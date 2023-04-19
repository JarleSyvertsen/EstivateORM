package hiof.gruppe1.testData;

public class Author {
    private int id;
    private String name;
    private String books;
    private String secret;
    private Page favoritePage;

    private Page leastFavoritePage;

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
