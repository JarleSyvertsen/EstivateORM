package hiof.gruppe1.Estivate.SQLAdapters;

import hiof.gruppe1.Estivate.Objects.SQLWriteObject;
import hiof.gruppe1.Estivate.drivers.IDriverHandler;

import java.sql.Connection;
import java.util.HashMap;

import static hiof.gruppe1.Estivate.SQLAdapters.TableDialectAttributeAdapter.convertToSQLDialect;
import static hiof.gruppe1.Estivate.utils.simpleTypeCheck.isSimple;

public class SQLTableManagment {
    IDriverHandler driver;

    public SQLTableManagment(IDriverHandler driver) {
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
        return true;
    }

    public String[] getDifferingColumns(SQLWriteObject writeObject) {
        return new String[]{};
    }
    public Boolean appendMissingColumns(SQLWriteObject writeObject) {
        return false;
    }

}
