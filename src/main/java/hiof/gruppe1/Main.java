package hiof.gruppe1;

import hiof.gruppe1.Estivate.EstivateCore.EstivatePersist;
import hiof.gruppe1.Estivate.drivers.IDriverHandler;
import hiof.gruppe1.Estivate.drivers.SQLiteDriver;
import hiof.gruppe1.Estivate.objectBuilders.EstivateBuilder;
import hiof.gruppe1.testData.Author;
import hiof.gruppe1.testData.Page;

public class Main {
    public static void main(String[] args) {
        EstivateBuilder estivateBuilder = new EstivateBuilder();
        EstivatePersist persist = estivateBuilder.setDBUrl("src/main/java/resources/estivateSQLite.db").build();
        Author perArne = new Author("Per Arne", "To tredjedel ved fra ORM");
        IDriverHandler driverHandler = new SQLiteDriver("src/main/java/resources/estivateSQLite.db");
        driverHandler.describeTable(Author.class);
        Page testPage = new Page(23, "Hello");
        perArne.setFavoritePage(testPage);
        persist.persist(perArne);
        Author retrieved = persist.getOne(4, Author.class);
        System.out.println(retrieved);
    }
}