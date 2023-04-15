package hiof.gruppe1.Estivate.drivers;

import java.sql.ResultSet;
import java.util.HashMap;

public interface IDriverHandler {
    public ResultSet executeQuery(String query);
    public void executeNoReturnSplit(String query);
    public HashMap<String, String> describeTable(Class classOfTable);
    public String getDialect();

    HashMap<String, String> describeTable(String simpleName);
}
