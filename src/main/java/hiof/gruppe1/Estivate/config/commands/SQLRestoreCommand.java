package hiof.gruppe1.Estivate.config.commands;

import java.sql.Connection;
import java.util.HashMap;

public class SQLRestoreCommand {
    String outputDirectory;
    String databaseToRestore;
    Connection connection;
    HashMap<String, String> arguments;

    public SQLRestoreCommand outputDirectory(String outputDirectory) {
        this.outputDirectory = outputDirectory;
        return this;
    }

    public SQLRestoreCommand restoreToDB(String database) {
        this.databaseToRestore = database;
        return this;
    }

    public void execute() {

    }
}
