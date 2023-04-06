package hiof.gruppe1.Estivate.SQLAdapters;

import hiof.gruppe1.Estivate.Objects.SQLWriteObject;

import java.sql.Connection;

public class SQLTableManagment {
    Connection connection;

    public SQLTableManagment(Connection connection) {
        this.connection = connection;
    }

    public Boolean insertIsTableCorrect(SQLWriteObject writeObject) {
        return false;
    }

    public String[] getDifferingColumns(SQLWriteObject writeObject) {
        return new String[]{};
    }
    public Boolean appendMissingColumns(SQLWriteObject writeObject) {
        return false;
    }

}
