package hiof.gruppe1;

import hiof.gruppe1.Estivate.EstivateCore.EstivatePersist;
import hiof.gruppe1.Estivate.objectBuilders.EstivateBuilder;
import hiof.gruppe1.testData.Author;
import hiof.gruppe1.testData.Page;

public class Main {
    public static void main(String[] args) {
        EstivateBuilder estivateBuilder = new EstivateBuilder();
        EstivatePersist persist = estivateBuilder.
                setDBUrl("src/main/java/resources/estivateSQLite.db")
                .setDebug(false)
                .build();
        Author perArne = new Author("Per Arne", "To tredjedel ved fra ORM");
        perArne.setId(1);
        Page testPage = new Page(23, "Hello");
        perArne.setFavoritePage(testPage);
        persist.persist(perArne);
        System.out.println(persist.getAll(Author.class));
    }
}