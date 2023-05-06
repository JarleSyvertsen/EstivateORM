package hiof.gruppe1.Estivate.SQLParsers.TextConcatenation;

import hiof.gruppe1.Estivate.Objects.SQLWriteObject;
import hiof.gruppe1.Estivate.drivers.IDriverHandler;

import java.util.HashMap;

import static hiof.gruppe1.Estivate.SQLAdapters.TableDialectAttributeAdapter.convertToSQLDialect;
import static hiof.gruppe1.Estivate.utils.simpleTypeCheck.isSimple;

public class TextConcatTableManagement {
    IDriverHandler driver;
    private final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS ";
    private final String ADD = "ADD ";

    public TextConcatTableManagement(IDriverHandler driver) {
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
    public String createAppendingTableIfMissing(String parentClass, String recursiveClass) {
        return doesRelationExist(parentClass, recursiveClass) ?
                createJoiningSyntax(parentClass, recursiveClass) : "";
    }
    public void createAppendingTableIfMissing(String parentClass, String recursiveClass, Boolean execute) {
        if(execute) {
            String appendingTableString = createAppendingTableIfMissing(parentClass, recursiveClass);
            if(!appendingTableString.isEmpty()) {
                driver.executeNoReturn(appendingTableString);
            }
        }
    }

    public Boolean doesRelationExist(String parentId, String childId) {
       String relationName = String.format("%s_has_%s", parentId, childId);
       return driver.describeTable(relationName).isEmpty();
    }

    public void createOrResizeTableIfNeeded(SQLWriteObject writeObject) {
        HashMap<String,String> SQLDescription = driver.describeTable(writeObject.getAttributeList().get("class").getInnerName());
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
        String tableName = ObjectToTable.getAttributeList().get("class").getInnerName();
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
        idMap.forEach((k,v) -> attributes.append(String.format("\"%s\" %s,", k, v)));
        idMap.forEach((k,v) -> foreignKeys.append(String.format(" FOREIGN KEY (\"%s\") REFERENCES %s (\"%s\") ON DELETE CASCADE", k, k, "id")));

        joiningString.append(CREATE_TABLE)
        .append(parentId)
        .append("_has_")
        .append(childId)
        .append(" ")
        .append("(")
        .append(attributes)
        .append(foreignKeys)
        .append(")")
        .append(";");

        return joiningString.toString();
    }

    private String createTableQuery(HashMap<String, String> tableAttributes, String tableName) {
        StringBuilder attributes = new StringBuilder();
        StringBuilder query = new StringBuilder();

        String TABLE_NAME = String.format("\"%s\" ", tableName);
        String PRIMARY_KEY = String.format("PRIMARY KEY(\"%s\" AUTOINCREMENT)", "id");

        tableAttributes.forEach((k,v) -> attributes.append(String.format("\"%s\" %s,", k, v)));

        query.append(CREATE_TABLE)
        .append(TABLE_NAME)
        .append("(")
        .append(attributes)
        .append(PRIMARY_KEY)
        .append(")")
        .append(";");

        return query.toString();
    }
    public void appendMissingColumns(SQLWriteObject writeObject, HashMap<String, String> difference) {
        String tableName = writeObject.getAttributeList().get("class").getInnerName();
        StringBuilder alterOperation = new StringBuilder();
        String ALTER_TABLE = "ALTER TABLE ";
        alterOperation.append(ALTER_TABLE);
        alterOperation.append(tableName);

        difference.forEach((name,type) -> alterOperation.append("\n")
        .append(ADD)
        .append(name)
        .append(" ")
        .append(type)
        .append(";"));

        driver.executeNoReturnSplit(alterOperation.toString());
    }

}
