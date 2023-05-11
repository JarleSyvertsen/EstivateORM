package hiof.gruppe1.testData;

import java.util.ArrayList;

public class Author {
    private int id;
    private String name;
    private String books;
    private String secret;
    private Page favoritePage;

    private Page leastFavoritePage;
    private Food favoriteFood;
    private ArrayList<Page> pages = new ArrayList<>();
    public Food getFavoriteFood() {
        return favoriteFood;
    }

    public ArrayList<Page> getPages() {
        return pages;
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

    public Author(String name, String books) {
        this.name = name;
        this.books = books;
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

    public String getBooks() {
        return books;
    }

    public void setBooks(String books) {
        this.books = books;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }
}
