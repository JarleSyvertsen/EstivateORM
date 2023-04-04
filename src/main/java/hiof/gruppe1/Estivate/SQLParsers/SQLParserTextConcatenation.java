package hiof.gruppe1.Estivate.SQLParsers;

import hiof.gruppe1.Estivate.Objects.SQLMultiCommand;
import hiof.gruppe1.Estivate.Objects.SQLWriteObject;

import java.sql.Connection;

public class SQLParserTextConcatenation implements ISQLParser {
    Connection connection;

    public Boolean writeToDatabase(SQLMultiCommand multiCommand) {
        return false;
    }

    public Boolean writeToDatabase(SQLWriteObject writeObject) {
        parseWriteObjectToDB(writeObject);
        return true;
    }

    private String parseWriteObjectToDB(SQLWriteObject writeObject) {
        String finalSQL;
        String insertInto = "INSERT INTO ";
        String insertTable = writeObject.getAttributeList().get("Class").getInnerClass();
        return insertTable;
    }


}
