package hiof.gruppe1.Estivate.drivers;

import java.io.File;
import java.sql.*;
import java.util.HashMap;

public class SQLiteDriver implements IDriverHandler {
    String relativeURL;

    public SQLiteDriver(String relativeURL) {
        this.relativeURL = relativeURL;
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
        String tableQuery = describeTableQuery(classOfTable);
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
    private String describeTableQuery(Class classOfTable) {
        String describeTable = "SELECT * FROM pragma_table_info('";
        StringBuilder describer = new StringBuilder();
        describer.append(describeTable);
        describer.append(classOfTable.getSimpleName());
        describer.append("')");
        return describer.toString();
    }
}
