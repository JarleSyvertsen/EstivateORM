package hiof.gruppe1.Estivate.config.commands;

import java.sql.Connection;
import java.util.HashMap;

public class SQLRestoreCommand {
    String outputDirectory;
    String databaseToRestore;
    Connection connection;
    String callingUser;
    HashMap<String, String> arguments;

    public SQLRestoreCommand outputDirectory(String outputDirectory) {
        this.outputDirectory = outputDirectory;
        return this;
    }

    public SQLRestoreCommand restoreToDB(String database) {
        this.databaseToRestore = database;
        return this;
    }
    public SQLRestoreCommand asUser(String username)  {
        return this;
    }


    public void execute() {

    }
}
