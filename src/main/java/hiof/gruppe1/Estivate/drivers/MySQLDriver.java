package hiof.gruppe1.Estivate.drivers;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.HashMap;

public class MySQLDriver implements IDriverHandler {
    private String dialect = "mysql";

    private Connection connect() {
        return null;
    }

    @Override
    public ResultSet executeQuery(String query) {
        return null;
    }

    @Override
    public void executeNoReturnSplit(String query) {

    }

    @Override
    public HashMap<String, String> describeTable(Class classOfTable) {
        return null;
    }

    @Override
    public ResultSet executeQueryIgnoreNoTable(String query) {
        return null;
    }

    @Override
    public String getDialect() {
        return dialect;
    }

    @Override
    public HashMap<String, String> describeTable(String simpleName) {
        return null;
    }
}
