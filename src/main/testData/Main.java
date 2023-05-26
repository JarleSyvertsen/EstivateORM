package hiof.gruppe1;

import hiof.gruppe1.Estivate.EstivateCore.EstivatePersist;
import hiof.gruppe1.Estivate.objectBuilders.EstivateBuilder;
import hiof.gruppe1.testData.*;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        EstivateBuilder estivateBuilder = new EstivateBuilder();
        EstivatePersist persist = estivateBuilder.
                setDBUrl("src/main/java/resources/estivateSQLite.db")
                .setDebug(false)
                .build();

        Author perArne = new Author("Per Arne");
        Book helved = new Book("Hel ved");
        perArne.addBook(helved);
        persist.persist(perArne);

        ArrayList<Author> alleForfattere = persist.getAll(Author.class);
    }
}