package hiof.gruppe1;

import hiof.gruppe1.Estivate.EstivateCore.EstivatePersist;
import hiof.gruppe1.Estivate.objectBuilders.EstivateBuilder;
import hiof.gruppe1.testData.Author;
import hiof.gruppe1.testData.Food;
import hiof.gruppe1.testData.Page;

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
        perArne.setFavoriteFood(pizza);
        Author perPer = new Author();
        Author perSecret = new Author("Per Secret", "sss");
        perPer.setName("Per Per");


        perSecret.setSecret("EEE");
        persist.persist(perPer);
        persist.persist(perArne);
        persist.persist(perSecret);
       ArrayList<Author> authors = persist.getAll(Author.class);
       System.out.println(authors);
    }
}