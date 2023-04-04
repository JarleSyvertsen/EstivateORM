package hiof.gruppe1;

import hiof.gruppe1.Estivate.Objects.SQLAttribute;
import hiof.gruppe1.Estivate.Objects.SQLWriteObject;
import hiof.gruppe1.Estivate.SQLParsers.SQLParserTextConcatenation;
import hiof.gruppe1.Estivate.drivers.IDriverHandler;
import hiof.gruppe1.Estivate.drivers.SQLiteDriver;
import hiof.gruppe1.Estivate.objectParsers.ReflectionParser;
import hiof.gruppe1.testData.Author;

import java.sql.Connection;
import java.util.HashMap;

public class Main {
    public static void main(String[] args) {
        ReflectionParser rp = new ReflectionParser();
        Author perArne = new Author("Per Arne", "To tredjedel ved");
        HashMap<String, SQLAttribute> attributes = rp.parseObjectToAttributeList(perArne);
        SQLWriteObject writeObject = new SQLWriteObject();
        writeObject.setAttributes(attributes);
        SQLParserTextConcatenation parse = new SQLParserTextConcatenation();
        IDriverHandler sqlDriver = new SQLiteDriver("src/main/java/resources/estivateSQLite.db");
        Connection connection = sqlDriver.connect();
        System.out.println(parse.parseWriteObjectToDB(writeObject));
    }
}