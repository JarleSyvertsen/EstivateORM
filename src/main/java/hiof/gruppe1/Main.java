package hiof.gruppe1;

import hiof.gruppe1.Estivate.EstivateCore.EstivatePersist;
import hiof.gruppe1.Estivate.Objects.SQLAttribute;
import hiof.gruppe1.Estivate.Objects.SQLWriteObject;
import hiof.gruppe1.Estivate.SQLParsers.SQLParserTextConcatenation;
import hiof.gruppe1.Estivate.drivers.IDriverHandler;
import hiof.gruppe1.Estivate.drivers.SQLiteDriver;
import hiof.gruppe1.Estivate.objectBuilders.EstivateBuilder;
import hiof.gruppe1.Estivate.objectParsers.ReflectionParser;
import hiof.gruppe1.testData.Author;

import java.sql.Connection;
import java.util.HashMap;

public class Main {
    public static void main(String[] args) {
        EstivateBuilder estivateBuilder = new EstivateBuilder();
        EstivatePersist persist = estivateBuilder.setDBUrl("src/main/java/resources/estivateSQLite.db").build();
        Author perArne = new Author("Per Arne", "To tredjedel ved");
        persist.persist(perArne);
    }
}