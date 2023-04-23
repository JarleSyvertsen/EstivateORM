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
        Author perSecret = new Author("Per Secret", "sss");
        perPer.setName("Per Per");
        perArne.setFavoriteFood(pizza);
        AuthorList authorList = new AuthorList();

        authorList.setTopAuthor(perArne);
        authorList.setName("TopList");

        AuthorListList ALL = new AuthorListList();
        ALL.setAaah("EEEH");
        ALL.setAuthorList(authorList);
        persist.persist(ALL);
        ArrayList<AuthorListList> authorListLists = persist.getAll(AuthorListList.class);
        System.out.println(authorListLists);
    }
}