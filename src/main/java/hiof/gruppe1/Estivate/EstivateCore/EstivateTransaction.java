package hiof.gruppe1.Estivate.EstivateCore;

import hiof.gruppe1.Estivate.Objects.SQLMultiCommand;
import hiof.gruppe1.Estivate.Objects.SQLSearchQuery;
import hiof.gruppe1.Estivate.Objects.SQLWriteObject;

import java.sql.Connection;
import java.util.Queue;

/**
 * An object returned from startTransaction function, supports persist, and getMany/getAggregate to make use of other functions in the ORM. These commands are queued up, and executed in one transaction when execute() is called.
 */
public class EstivateTransaction {
    Queue<SQLWriteObject> writeObjects;
    Queue<SQLMultiCommand> multiCommands;
    Queue<SQLSearchQuery> searchQueries;

    Connection connection;

    public EstivateTransaction startTransaction() {
        // SQL Start transaction
        return this;
    }

    public void persist(Object object) {
        return;
    }

    public SQLSearchQuery getMany() {return null;}
    public SQLMultiCommand getAggregate() {return null;}
    public void commit() {
    }

    public void endTransaction() {
        // End transaction
        // Could empty out variables as well, considering this object should not be reused.
    }
}
