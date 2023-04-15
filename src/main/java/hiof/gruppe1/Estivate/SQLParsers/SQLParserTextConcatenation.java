package hiof.gruppe1.Estivate.SQLParsers;

import hiof.gruppe1.Estivate.Objects.SQLAttribute;
import hiof.gruppe1.Estivate.Objects.SQLMultiCommand;
import hiof.gruppe1.Estivate.Objects.SQLWriteObject;
import hiof.gruppe1.Estivate.SQLAdapters.SQLTableCalculations;
import hiof.gruppe1.Estivate.drivers.IDriverHandler;
import hiof.gruppe1.Estivate.objectParsers.IObjectParser;
import hiof.gruppe1.Estivate.objectParsers.ReflectionParser;

import static hiof.gruppe1.Estivate.SQLAdapters.TableDialectAttributeAdapter.getCompatAttr;
import static hiof.gruppe1.Estivate.utils.simpleTypeCheck.isSimple;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class SQLParserTextConcatenation implements ISQLParser {
    private final String SELECT = "SELECT ";
    private final String FROM = "FROM ";
    private final String SELECT_ALL_FROM = "SELECT * FROM ";
    private final String INSERT_INTO = "INSERT OR REPLACE INTO ";
    private final String VALUES = " VALUES ";
    private final String WHERE = " WHERE ";
    private final String ID_EQUALS = "id = ";
    private final String SET_PARENT = "\nUPDATE tempRelations SET parent = last_insert_rowid();";
    private final String SET_CHILD = "\nUPDATE tempRelations SET child = last_insert_rowid();";

    IDriverHandler sqlDriver;
    IObjectParser objectParser;
    SQLTableCalculations tableManagement;

    public SQLParserTextConcatenation(IDriverHandler sqlDriver) {
        this.sqlDriver = sqlDriver;
        this.objectParser = new ReflectionParser();
        this.tableManagement = new SQLTableCalculations(sqlDriver);
    }

    public static String getObjectClass(SQLWriteObject writeObject) {
        return writeObject.getAttributeList().get("class").getInnerClass();
    }

    public Boolean writeToDatabase(SQLMultiCommand multiCommand) {
        return false;
    }

    public Boolean writeToDatabase(SQLWriteObject writeObject) {
        String writeableString = createWritableSQLString(writeObject);
        sqlDriver.executeNoReturnSplit(writeableString);
        return true;
    }

    public <T> T readFromDatabase(Class<T> castTo, int id) {
        String SQLQuery = createReadableSQLString(castTo, id);
        HashMap<String, String> describedTable = sqlDriver.describeTable(castTo);
        ResultSet querySet = sqlDriver.executeQuery(SQLQuery);

        HashMap<String, SQLAttribute> readAttributes = getAttributeMap(describedTable, querySet);
        return objectParser.parseAttributeListToObject(castTo, readAttributes);
    }

    public <T> ArrayList<T> readFromDatabase(Class<T> castTo) {
        String SQLQuery = createReadableSQLString(castTo);
        HashMap<String, String> describedTable = sqlDriver.describeTable(castTo);
        ResultSet querySet = sqlDriver.executeQuery(SQLQuery);

        ArrayList<T> arrayOfObjects = new ArrayList<>();
        try {
            while (querySet.next()) {
                T object = objectParser.parseAttributeListToObject(castTo, getAttributeMap(describedTable, querySet));
                arrayOfObjects.add(object);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return arrayOfObjects;
    }

    private String createReadableSQLString(Class queryClass, int id) {
        StringBuilder limiter = new StringBuilder();
        limiter.append(WHERE);
        limiter.append(ID_EQUALS);
        limiter.append(id);
        return createReadableSQLString(queryClass, limiter.toString());
    }

    private String createReadableSQLString(Class queryClass, String limiter) {
        StringBuilder reader = new StringBuilder();
        reader.append(SELECT_ALL_FROM);
        reader.append(queryClass.getSimpleName());
        reader.append(limiter != null ? limiter : "");
        return reader.toString();
    }

    private String createReadableSQLString(Class queryClass) {
        StringBuilder reader = new StringBuilder();
        reader.append(SELECT_ALL_FROM);
        reader.append(queryClass.getSimpleName());
        return reader.toString();
    }

    private String createWritableSQLString(SQLWriteObject writeObject) {
        if (!tableManagement.insertIsTableCorrect(writeObject)) {
            tableManagement.createTable(writeObject);
        }

        if (writeObject.getAttributeList().get("id").getData().toString().equals("0")) {
            writeObject.getAttributeList().remove("id");
        }

        String insertTable = getObjectClass(writeObject);
        writeObject.getAttributeList().remove("class");

        // PartBuilders to allow building the String in a non-linear way.
        StringBuilder finalString = new StringBuilder();
        StringBuilder keyString = new StringBuilder();
        StringBuilder valuesString = new StringBuilder();
        StringBuilder recursiveAdds = traverseNonPrimitives(writeObject, insertTable);
        // Remove the complex objects after parsing.
        writeObject.getAttributeList().entrySet().removeIf(entry -> !isSimple(entry.getValue().getData().getClass()));

        if(!recursiveAdds.isEmpty()) {
            String TEMP_JOINING_TABLE = "CREATE TEMP TABLE IF NOT EXISTS tempRelations (Id PRIMARY KEY, parent INTEGER, child INTEGER); \n" +
                    "INSERT OR IGNORE INTO tempRelations VALUES (0,0,0); \n";
            finalString.append(TEMP_JOINING_TABLE);
        }

        // Appends
        finalString.append(INSERT_INTO);
        finalString.append(insertTable);

        writeObject.getAttributeList().forEach((k, v) -> {
            keyString.append(k);
            keyString.append(",");
            valuesString.append(createWritableValue(v));
            valuesString.append(",");
        });

        finalString.append(createValuesInParenthesis(keyString));
        finalString.append(VALUES);
        finalString.append(createValuesInParenthesis(valuesString));
        finalString.append(";");
        if(!recursiveAdds.isEmpty()) {
            finalString.append(SET_PARENT);
        }
        finalString.append(recursiveAdds);

        return finalString.toString();
    }

    private StringBuilder traverseNonPrimitives(SQLWriteObject writeObject, String parentClass) {
        StringBuilder recursiveAdds = new StringBuilder();

        writeObject.getAttributeList().forEach((k, v) -> {
            if (!isSimple(v.getData().getClass())) {
                HashMap<String, SQLAttribute> parsedAttributes = objectParser.parseObjectToAttributeList(v.getDataRaw());

                SQLWriteObject recursiveObject = new SQLWriteObject(parsedAttributes);
                String recursiveRelationship = createRelationshipInsert(parentClass, getObjectClass(recursiveObject));
                String appendingTable = tableManagement.createAppendingTableIfMissing(parentClass, recursiveObject);

                recursiveAdds.append("\n");
                recursiveAdds.append(appendingTable);
                recursiveAdds.append("\n");
                recursiveAdds.append(createWritableSQLString(recursiveObject));
                recursiveAdds.append(SET_CHILD);
                recursiveAdds.append(recursiveRelationship);
            }
        });
        return recursiveAdds;
    }

    private String createRelationshipInsert(String parentId, String childId) {
        StringBuilder relationshipInsert = new StringBuilder();
        StringBuilder keys = new StringBuilder();
        keys.append(parentId);
        keys.append(",");
        keys.append(childId);
        keys.append(" ");

        relationshipInsert.append("\n");
        relationshipInsert.append(INSERT_INTO);
        relationshipInsert.append(parentId);
        relationshipInsert.append("_has_");
        relationshipInsert.append(childId);
        relationshipInsert.append(createValuesInParenthesis(keys));
        relationshipInsert.append(" ");
        relationshipInsert.append(SELECT);
        relationshipInsert.append("parent");
        relationshipInsert.append(",");
        relationshipInsert.append("child ");
        relationshipInsert.append(FROM);
        relationshipInsert.append("tempRelations");

        return relationshipInsert.toString();
    }


    private String createValuesInParenthesis(StringBuilder keyString) {
        StringBuilder finalString = new StringBuilder();
        finalString.append("(");
        finalString.append(keyString);
        finalString.deleteCharAt(finalString.length() - 1);
        finalString.append(")");
        return finalString.toString();
    }

    private static String formatLevelParent(int level) {
        return String.format("level_%d_parent", level);
    }

    private static String formatLevelReference(int level) {
        return String.format("level_%d_id", level);
    }

    private String createWritableValue(SQLAttribute sqlAttr) {
        if (sqlAttr.getData().getClass().getSimpleName().equals("String")) {
            return String.format("\"%s\"", sqlAttr.getData().toString());
        }
        return sqlAttr.getData().toString();
    }

    private static HashMap<String, SQLAttribute> getAttributeMap(HashMap<String, String> describedTable, ResultSet querySet) {
        HashMap<String, SQLAttribute> readAttributes = new HashMap<>();
        try {
            for (Map.Entry<String, String> entry : describedTable.entrySet()) {
                String attributeName = entry.getKey();
                String attributeValue = entry.getValue();
                switch (getCompatAttr(attributeValue)) {
                    case INT_COMPAT -> readAttributes.put(attributeName, new SQLAttribute(Integer.class, querySet.getInt(attributeName)));
                    case STRING_COMPAT -> readAttributes.put(attributeName, new SQLAttribute(String.class, querySet.getString(attributeName)));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return readAttributes;
    }


}
