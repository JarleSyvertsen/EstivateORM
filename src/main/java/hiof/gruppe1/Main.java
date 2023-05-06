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
                persist.startAggregate()
                .count(Author.class, null, "cAuthors")
                .count(Page.class, null, "cPages")
                .result("cPages / cAuthors");

        System.out.println(averagePagePerAuthor);
         ArrayList<Author> id2 = persist.getMany()
                 .addCondition("WHERE author_name = 'Per Arne'")
                 .asArrayList(Author.class);
        System.out.println(id2);
    }
}