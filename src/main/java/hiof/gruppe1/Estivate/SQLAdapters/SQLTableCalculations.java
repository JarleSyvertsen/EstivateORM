package hiof.gruppe1.Estivate.SQLAdapters;

import hiof.gruppe1.Estivate.Objects.SQLWriteObject;
import hiof.gruppe1.Estivate.drivers.IDriverHandler;

import java.util.HashMap;

import static hiof.gruppe1.Estivate.SQLAdapters.TableDialectAttributeAdapter.convertToSQLDialect;
import static hiof.gruppe1.Estivate.utils.simpleTypeCheck.isSimple;

public class SQLTableCalculations {
    IDriverHandler driver;

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

    public Boolean insertIsTableCorrect(SQLWriteObject writeObject) {
        HashMap<String,String> SQLDescription = driver.describeTable(writeObject.getAttributeList().get("class").getInnerClass());
        HashMap<String, String> writeObjectDescription = getWriteDescription(writeObject);
        return SQLDescription.equals(writeObjectDescription);
    }

    public Boolean createTable(SQLWriteObject ObjectToTable) {
        String tableName = ObjectToTable.getAttributeList().get("class").getInnerClass();
        String createQuery = createTableQuery(getWriteDescription(ObjectToTable), tableName);
        driver.executeNoReturn(createQuery);
        return true;
    }

    private String createTableQuery(HashMap<String, String> tableAttributes, String tableName) {
        String CREATE_TABLE = "CREATE TABLE ";
        String TABLE_NAME = String.format("\"%s\" ", tableName);
        String PRIMARY_KEY = "PRIMARY KEY(\"id\" AUTOINCREMENT)";
        StringBuilder attributes = new StringBuilder();
        StringBuilder query = new StringBuilder();
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
