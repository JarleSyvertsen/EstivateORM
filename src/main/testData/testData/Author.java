package hiof.gruppe1.testData;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Author {
    private int id;
    private String name;
    private String secret;
    private Page favoritePage;

    private Page leastFavoritePage;
    private Food favoriteFood;
    private ArrayList<Page> pages = new ArrayList<>();
    private Set<Book> books = new HashSet<>();
    public Food getFavoriteFood() {
        return favoriteFood;
    }

    public ArrayList<Page> getPages() {
        return pages;
    }
    public void addBook(Book book) {
        books.add(book);
    }

    public Set<Book> getBooks() {
        return books;
    }

    public void setBooks(Set<Book> books) {
        this.books = books;
    }

    public void setPages(ArrayList<Page> pages) {
        this.pages = pages;
    }

    public void addPage(Page page) {
        pages.add(page);
    }

    public void setFavoriteFood(Food favoriteFood) {
        this.favoriteFood = favoriteFood;
    }

    public Page getLeastFavoritePage() {
        return leastFavoritePage;
    }

    public void setLeastFavoritePage(Page leastFavoritePage) {
        this.leastFavoritePage = leastFavoritePage;
    }

    public Page getFavoritePage() {
        return favoritePage;
    }

    public void setFavoritePage(Page favoritePage) {
        this.favoritePage = favoritePage;
    }

    public Author() {
    }
    public Author(String name) {
        this.name = name;
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }
}
