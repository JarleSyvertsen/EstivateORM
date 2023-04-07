package hiof.gruppe1.Estivate.drivers;

import java.io.File;
import java.sql.*;
import java.util.HashMap;

public class SQLiteDriver implements IDriverHandler {
    private String relativeURL;
    private String dialect = "sqlite";

    public SQLiteDriver(String relativeURL) {
        this.relativeURL = relativeURL;
    }

    public String getDialect() {
        return dialect;
    }

    private Connection connect() {
        Connection connection = null;
        try {
            String url = new File(relativeURL).getAbsolutePath();
            String connectionURL = "jdbc:sqlite:" + url;

            connection = DriverManager.getConnection(connectionURL);

            // JDBC ignores certain constrains as default for backwards compatability,
            // we explicitly opt into foreign key constrains for all DB functions.
            Statement respectConstraints = connection.createStatement();
            respectConstraints.executeUpdate("PRAGMA foreign_keys = ON;");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return connection;
    }

    public void executeNoReturn(String query) {
        try {
            Connection connection = connect();
            PreparedStatement executeStatement = connection.prepareStatement(query);
            executeStatement.execute();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public ResultSet executeQuery(String query) {
        ResultSet rs = null;
        try {
            Connection connection = connect();
            PreparedStatement selectStatement = connection.prepareStatement(query);
            rs = selectStatement.executeQuery();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return rs;
    }

    @Override
    public void executeInsert(String query) {
        ResultSet closingStatement = executeQuery(query);
        try {
            closingStatement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public HashMap<String, String> describeTable(Class classOfTable) {
       return describeTable(classOfTable.getSimpleName());
    }
    @Override
    public HashMap<String, String> describeTable(String simpleName) {
        String tableQuery = describeTableQuery(simpleName);
        ResultSet tableResult = executeQuery(tableQuery);

        HashMap<String, String> tableInfo = new HashMap<>();
        try {
            while (tableResult.next()) {
                tableInfo.put(tableResult.getString("name"), tableResult.getString("type"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return tableInfo;
    }
    private String describeTableQuery(String simpleName) {
        String describeTable = "SELECT * FROM pragma_table_info('";
        StringBuilder describer = new StringBuilder();
        describer.append(describeTable);
        describer.append(simpleName);
        describer.append("')");
        return describer.toString();
    }
}
