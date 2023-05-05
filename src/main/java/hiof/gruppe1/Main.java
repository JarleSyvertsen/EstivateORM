package hiof.gruppe1;

import hiof.gruppe1.Estivate.EstivateCore.EstivatePersist;
import hiof.gruppe1.Estivate.objectBuilders.EstivateBuilder;
import hiof.gruppe1.testData.*;

public class Main {
    public static void main(String[] args) {
        EstivateBuilder estivateBuilder = new EstivateBuilder();
        EstivatePersist persist = estivateBuilder.
                setDBUrl("src/main/java/resources/estivateSQLite.db")
                .setDebug(false)
                .build();
        Author perArne = new Author("Per Arne", "To tredjedel");
        Page testPage = new Page(23, "Hello");
        Page testPage2 = new Page(24, "Hi");
        perArne.setFavoritePage(testPage);
        perArne.setLeastFavoritePage(testPage2);
        Food pizza = new Food();
        pizza.setName("Pizza");
        pizza.setTaste("Great");
        Author perPer = new Author();
        perPer.setName("Per Per");
        perArne.setFavoriteFood(pizza);
        AuthorList authorList = new AuthorList();

        authorList.setTopAuthor(perArne);
        authorList.setName("TopList");

        Double averagePagePerAuthor =
                persist.startAggregateTransaction()
                .count(Author.class, null, "cAuthors")
                .count(Page.class, null, "cPages")
                .result("cPages / cAuthors");

        System.out.println(averagePagePerAuthor);
    }
}