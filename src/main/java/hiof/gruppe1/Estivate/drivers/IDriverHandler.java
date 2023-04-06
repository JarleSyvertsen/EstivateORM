package hiof.gruppe1.Estivate.drivers;

import java.sql.ResultSet;
import java.util.HashMap;

public interface IDriverHandler {
    public ResultSet executeQuery(String query);
    public void executeInsert(String query);
    public HashMap<String, String> describedTable(Class classOfTable);
}
