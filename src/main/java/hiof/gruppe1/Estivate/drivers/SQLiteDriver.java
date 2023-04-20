package hiof.gruppe1.Estivate.drivers;

import java.io.File;
import java.sql.*;
import java.util.HashMap;

public class SQLiteDriver implements IDriverHandler {
    private String relativeURL;
    private String dialect = "sqlite";
    private Boolean debug = false;
    public SQLiteDriver(String relativeURL, Boolean debug) {
        this.relativeURL = relativeURL;
        this.debug = debug;
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

            // To allow replacing values in spot, constraints are used as default
            // Proper usage would probably be introducing cascading deletes while replacing
            // This is to no massively complicate the code with calculating the most optimal
            // action, and instead recreating objects when needed.

            Statement pragmaSettings = connection.createStatement();
            pragmaSettings.executeUpdate("PRAGMA foreign_keys = ON;");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return connection;
    }

    public void executeNoReturnSplit(String query) {
        if(debug) {
            System.out.println("query: \n" + query);
            return;
        }

        String[] splitQuery = query.split(";");

        try {
            Connection connection = connect();
            for(String semicolonSeparatedQuery : splitQuery) {
                PreparedStatement executeStatement = connection.prepareStatement(semicolonSeparatedQuery);
                executeStatement.execute();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private ResultSet executeQueryBase(String query) throws SQLException {
        ResultSet rs;
        String executingQuery = query;
        // Creating an empty rs is non-trivial, we instead rely on an empty search
        // Though in practice, this should be handled without hitting the database.
        if(debug) {
            System.out.println("query: \n" + query);
            executingQuery = "SELECT 1 WHERE false";
        }
        Connection connection = connect();
        PreparedStatement selectStatement = connection.prepareStatement(executingQuery);
        rs = selectStatement.executeQuery();
        return rs;
    }

    public ResultSet executeQuery(String query) {
        try {
            return executeQueryBase(query);
        } catch (SQLException e) {
            System.out.println(e);
        }
        return null;
    }
    public ResultSet executeQueryIgnoreNoTable(String query) {
        try {
           return executeQueryBase(query);
        } catch (SQLException e) {
        }
        return null;
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
