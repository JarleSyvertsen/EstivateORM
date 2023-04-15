package hiof.gruppe1.Estivate.SQLAdapters;

import hiof.gruppe1.Estivate.Objects.SQLWriteObject;
import hiof.gruppe1.Estivate.drivers.IDriverHandler;

import java.util.HashMap;

import static hiof.gruppe1.Estivate.SQLAdapters.TableDialectAttributeAdapter.convertToSQLDialect;
import static hiof.gruppe1.Estivate.SQLParsers.SQLParserTextConcatenation.getObjectClass;
import static hiof.gruppe1.Estivate.utils.simpleTypeCheck.isSimple;

public class SQLTableCalculations {
    IDriverHandler driver;
    private String CREATE_TABLE = "CREATE TABLE ";

    public SQLTableCalculations(IDriverHandler driver) {
        this.driver = driver;
    }

    private HashMap<String, String> getWriteDescription(SQLWriteObject writeObject) {
        HashMap<String, String> writeObjectDescription = new HashMap<>();
        writeObject.getAttributeList().forEach((k, v) -> {
            if(isSimple(v.getData().getClass())) {
                writeObjectDescription.put(k, convertToSQLDialect(v.getData().getClass(), driver.getDialect()));
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

    public Boolean insertIsTableCorrect(SQLWriteObject writeObject) {
        HashMap<String,String> SQLDescription = driver.describeTable(writeObject.getAttributeList().get("class").getInnerClass());
        HashMap<String, String> writeObjectDescription = getWriteDescription(writeObject);
        return SQLDescription.equals(writeObjectDescription);
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
        idMap.forEach((k,v) -> {
            attributes.append(String.format("\"%s\" %s,", k, v));
        });

        idMap.forEach((k,v) -> {
            foreignKeys.append(String.format(" FOREIGN KEY (%s) REFERENCES %s (%s) ON DELETE CASCADE", k, k.toLowerCase(), "id"));
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
        String PRIMARY_KEY = "PRIMARY KEY(\"id\" AUTOINCREMENT)";

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


    public String[] getDifferingColumns(SQLWriteObject writeObject) {
        return new String[]{};
    }
    public Boolean appendMissingColumns(SQLWriteObject writeObject) {
        return false;
    }

}
