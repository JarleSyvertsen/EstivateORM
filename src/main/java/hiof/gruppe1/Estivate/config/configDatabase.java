package hiof.gruppe1.Estivate.config;

import hiof.gruppe1.Estivate.config.commands.SQLDumpCommand;
import hiof.gruppe1.Estivate.config.commands.SQLRestoreCommand;

import java.sql.Connection;

public class configDatabase {
    Connection connection;

    public SQLDumpCommand startSQLDump() {
        return new SQLDumpCommand();
    }

    public SQLRestoreCommand restoreSQLDump() {
        return new SQLRestoreCommand();
    }

    public configDatabase getWorkingDatabase() {
        return this;
    }

    public configDatabase startTransaction() {
        return this;
    }
}
