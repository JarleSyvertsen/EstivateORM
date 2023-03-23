package hiof.gruppe1.Estivate.config.commands;

import java.sql.Connection;
import java.util.HashMap;

public class SQLDumpCommand {
    HashMap<String, String> arguments;
    String currentWorkingFile;
    Connection connection;

    public SQLDumpCommand setOutputLocation(String filename) {
        this.currentWorkingFile = filename;
        return this;
    }

    public void execute() {

    }
}
