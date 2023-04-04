package hiof.gruppe1.Estivate.config;

import hiof.gruppe1.Estivate.config.commands.SQLDumpCommand;
import hiof.gruppe1.Estivate.config.commands.SQLRestoreCommand;

import java.sql.Connection;

/**
 * A subset of the config namespace that provides methods mapping to SQL functions that affect the database as a whole.
 */
public class configDatabase {
    Connection connection;

    public configDatabase() {
    }

    public configDatabase(Connection connection) {
        this.connection = connection;
    }

    /**
     * Provides an object that allows defining settings for the SQL dump command. This allows the user to define output location for the SQL dump file via setOutputLocation. Executes via the execute() command.
     * @return SQLDumpCommand dumpCommand
     */
    public SQLDumpCommand startSQLDump() {
        return new SQLDumpCommand();
    }

    /**
     *
     Provides an object where the user can define the location of the SQL dump file, and which database to store the result of the dump file. In addition, the user can define the working user for the command, to support running with elevated permissions.
     * @return
     */
    public SQLRestoreCommand restoreSQLDump() {
        return new SQLRestoreCommand();
    }

    public configDatabase getWorkingDatabase() {
        return this;
    }

    /**
     * Required for all configDatabase transactions, this method starts a new SQL transaction, and provides an object with methods that allows addressing the SQL database directly.
     * @return configDatabase dbConfigObject
     */
    public configDatabase startTransaction() {
        return this;
    }
}
