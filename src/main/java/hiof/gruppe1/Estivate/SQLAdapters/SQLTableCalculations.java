package hiof.gruppe1.Estivate.SQLAdapters;

import hiof.gruppe1.Estivate.Objects.SQLWriteObject;
import hiof.gruppe1.Estivate.drivers.IDriverHandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import static hiof.gruppe1.Estivate.SQLAdapters.TableDialectAttributeAdapter.convertToSQLDialect;
import static hiof.gruppe1.Estivate.SQLParsers.SQLParserTextConcatenation.getObjectClass;
import static hiof.gruppe1.Estivate.utils.simpleTypeCheck.isSimple;

public class SQLTableCalculations {
    IDriverHandler driver;
    private String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS ";
    private String SELECT_FROM_SCHEMA = "SELECT name FROM sqlite_schema ";
    private String WHERE = "WHERE ";
    private String LIKE = "LIKE ";
    private String NOT_LIKE = "NOT LIKE ";
    private String AND = "AND ";
    private String NAME = "name ";
    private String LEFT_JOIN = "LEFT JOIN ";
    private String UNDERSCORE = "_";
    private String ON = " ON ";
    private String EQUALS =  " = ";
    private String PERIOD = ".";
    private String ID = "id";
    private String NEW_LINE = "\n";
    private String ALTER_TABLE = "ALTER TABLE ";
    private String ADD = "ADD ";

    public SQLTableCalculations(IDriverHandler driver) {
        this.driver = driver;
    }

    private HashMap<String, String> getWriteDescription(SQLWriteObject writeObject) {
        HashMap<String, String> writeObjectDescription = new HashMap<>();
        String tableName = writeObject.getAttributeList().get("class").getInnerClass();
        writeObject.getAttributeList().forEach((k, v) -> {
            if(isSimple(v.getData().getClass())) {
                writeObjectDescription.put(tableName + "_" + k, convertToSQLDialect(v.getData().getClass(), driver.getDialect()));
            }
        });
        return writeObjectDescription;
    }

    public String createAppendingTableIfMissing(String parentClass, SQLWriteObject recursiveObject) {
        return doesRelationExist(parentClass, getObjectClass(recursiveObject)) ?
                createJoiningSyntax(parentClass, getObjectClass(recursiveObject)) : "";
    }

    public Boolean doesRelationExist(String parentId, String childId) {
       String relationName = String.format("%s_has_%s", parentId, childId);
       return driver.describeTable(relationName).isEmpty();
    }

    public void createOrResizeTableIfNeeded(SQLWriteObject writeObject) {
        HashMap<String,String> SQLDescription = driver.describeTable(writeObject.getAttributeList().get("class").getInnerClass());
        HashMap<String, String> writeObjectDescription = getWriteDescription(writeObject);
        HashMap<String, String> difference = new HashMap<>(writeObjectDescription);
        difference.keySet().removeAll(SQLDescription.keySet());
        if(SQLDescription.isEmpty()) {
            createTable(writeObject);
            return;
        }
        if(!difference.isEmpty()) {
            appendMissingColumns(writeObject, difference);
        }
    }

    public void createTable(SQLWriteObject ObjectToTable) {
        String tableName = ObjectToTable.getAttributeList().get("class").getInnerClass();
        String createQuery = createTableQuery(getWriteDescription(ObjectToTable), tableName);
        driver.executeNoReturnSplit(createQuery);
    }

    public String createJoiningSyntax(String parentId, String childId) {
        StringBuilder joiningString = new StringBuilder();
        StringBuilder attributes = new StringBuilder();
        StringBuilder foreignKeys = new StringBuilder();

        HashMap<String, String> idMap = new HashMap<>();
        idMap.put(parentId, "INTEGER");
        idMap.put(childId, "INTEGER");
        attributes.append(String.format("\"%s\" %s,", "setter", "TEXT"));
        idMap.forEach((k,v) -> {
            attributes.append(String.format("\"%s\" %s,", k, v));
        });

        idMap.forEach((k,v) -> {
            foreignKeys.append(String.format(" FOREIGN KEY (\"%s\") REFERENCES %s (\"%s_%s\") ON DELETE CASCADE", k, k, k, "id"));
        });

        joiningString.append(CREATE_TABLE);
        joiningString.append(parentId);
        joiningString.append("_has_");
        joiningString.append(childId);
        joiningString.append(" ");
        joiningString.append("(");
        joiningString.append(attributes);
        joiningString.append(foreignKeys);
        joiningString.append(")");
        joiningString.append(";");


        return joiningString.toString();
    }

    private String createTableQuery(HashMap<String, String> tableAttributes, String tableName) {
        StringBuilder attributes = new StringBuilder();
        StringBuilder query = new StringBuilder();

        String TABLE_NAME = String.format("\"%s\" ", tableName);
        String PRIMARY_KEY = String.format("PRIMARY KEY(\"%s_%s\" AUTOINCREMENT)", tableName, "id");

        tableAttributes.forEach((k,v) -> {
            attributes.append(String.format("\"%s\" %s,", k.toString(), v.toString()));
        });

        query.append(CREATE_TABLE);
        query.append(TABLE_NAME);
        query.append("(");
        query.append(attributes);
        query.append(PRIMARY_KEY);
        query.append(")");
        query.append(";");

        return query.toString();
    }
    private ArrayList<String> getRelatingTables(String className) {
        ArrayList<String> relatedTables = new ArrayList<>();

        StringBuilder selectRelatedTables = new StringBuilder();
        selectRelatedTables.append(SELECT_FROM_SCHEMA);
        selectRelatedTables.append(WHERE);
        selectRelatedTables.append(NAME);
        selectRelatedTables.append(LIKE);
        selectRelatedTables.append(String.format("'%s%%'", className));
        selectRelatedTables.append(AND);
        selectRelatedTables.append(NAME);
        selectRelatedTables.append(NOT_LIKE);
        selectRelatedTables.append(String.format("'%s'", className));

        ResultSet resultSet = driver.executeQuery(selectRelatedTables.toString());
        try {
            while (resultSet.next()) {
                relatedTables.add(resultSet.getString("name"));
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return relatedTables;
    }
    public ArrayList<String> getRelatedTables(String className) {
        ArrayList<String> referencedTables = new ArrayList<>();
        getRelatingTables(className).forEach((referenceTable) -> referencedTables.add(referenceTable.substring(referenceTable.lastIndexOf("_") + 1)));
        return referencedTables;
    }

    public String createJoiningTables(String className) {
        ArrayList<String> connectedTables = getRelatingTables(className);

        StringBuilder joiningTableQuery = new StringBuilder();
        connectedTables.forEach((complete_name) -> {
            String referenced_table = complete_name.substring(complete_name.lastIndexOf("_") + 1);
            String referencing_table = complete_name.substring(0,complete_name.indexOf("_"));
            // Joining Table
            joiningTableQuery.append(LEFT_JOIN);
            joiningTableQuery.append(complete_name);
            joiningTableQuery.append(ON);
            joiningTableQuery.append(complete_name);
            joiningTableQuery.append(PERIOD);
            joiningTableQuery.append(referencing_table);
            joiningTableQuery.append(EQUALS);
            joiningTableQuery.append(referencing_table);
            joiningTableQuery.append(PERIOD);
            joiningTableQuery.append(referencing_table);
            joiningTableQuery.append(UNDERSCORE);
            joiningTableQuery.append(ID);


            // Right-Table
            joiningTableQuery.append(NEW_LINE);
            joiningTableQuery.append(LEFT_JOIN);
            joiningTableQuery.append(referenced_table);
            joiningTableQuery.append(ON);
            joiningTableQuery.append(referenced_table);
            joiningTableQuery.append(PERIOD);
            joiningTableQuery.append(referenced_table);
            joiningTableQuery.append(UNDERSCORE);
            joiningTableQuery.append(ID);
            joiningTableQuery.append(EQUALS);
            joiningTableQuery.append(complete_name);
            joiningTableQuery.append(PERIOD);
            joiningTableQuery.append(referenced_table);
        });
        return joiningTableQuery.toString();
    }
    public void appendMissingColumns(SQLWriteObject writeObject, HashMap<String, String> difference) {
        String tableName = writeObject.getAttributeList().get("class").getInnerClass();
        StringBuilder alterOperation = new StringBuilder();
        alterOperation.append(ALTER_TABLE);
        alterOperation.append(tableName);
        difference.forEach((name,type) -> {
            alterOperation.append("\n");
            alterOperation.append(ADD);
            alterOperation.append(tableName);
            alterOperation.append(".");
            alterOperation.append(name);
            alterOperation.append(" ");
            alterOperation.append(type);
            alterOperation.append(";");
        });
        driver.executeNoReturnSplit(alterOperation.toString());
    }

}
