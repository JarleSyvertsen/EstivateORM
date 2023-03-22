package hiof.gruppe1.Estivate.EstivateCore;

import hiof.gruppe1.Estivate.Objects.SQLMultiCommand;
import hiof.gruppe1.Estivate.Objects.SQLWriteObject;

import java.sql.Connection;
import java.util.Queue;

public class EstivateTransaction {
    Queue<SQLWriteObject> writeObjects;
    Queue<SQLMultiCommand> multiCommands;
    Connection connection;

    public EstivateTransaction startTransaction() {
        // SQL Start transaction
        return this;
    }

    public void persist(Object object) {
        return;
    }

    public void commit() {
    }

    public void endTransaction() {
        // End transaction
        // Could empty out variables as well, considering this object should not be reused.
    }
}
