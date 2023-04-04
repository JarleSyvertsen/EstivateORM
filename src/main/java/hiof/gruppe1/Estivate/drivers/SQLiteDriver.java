package hiof.gruppe1.Estivate.drivers;

import hiof.gruppe1.Main;

import java.io.File;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLiteDriver implements IDriverHandler {
    String relativeURL;

    public SQLiteDriver(String relativeURL) {
        this.relativeURL = relativeURL;
    }
    public Connection connect() {
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
}
