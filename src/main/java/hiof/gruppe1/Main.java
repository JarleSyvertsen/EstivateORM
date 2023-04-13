package hiof.gruppe1;

import hiof.gruppe1.Estivate.EstivateCore.EstivatePersist;
import hiof.gruppe1.Estivate.drivers.IDriverHandler;
import hiof.gruppe1.Estivate.drivers.SQLiteDriver;
import hiof.gruppe1.Estivate.objectBuilders.EstivateBuilder;
import hiof.gruppe1.testData.Author;
import hiof.gruppe1.testData.Page;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        EstivateBuilder estivateBuilder = new EstivateBuilder();
        EstivatePersist persist = estivateBuilder.
                setDBUrl("src/main/java/resources/estivateSQLite.db")
                .setDebug(true)
                .build();
        Author perArne = new Author("Per Arne", "To tredjedel ved fra ORM");
        Page testPage = new Page(23, "Hello");
        perArne.setFavoritePage(testPage);
        persist.persist(perArne);
        ArrayList<Author> authors = persist.getAll(Author.class);
        Author perArne2 = persist.getOne(1, Author.class);
        System.out.println(authors);
    }
}