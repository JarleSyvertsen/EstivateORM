package hiof.gruppe1.testData;

public class Author {
    private int id;
    private String name;
    private String books;

    public Author() {
    }

    public Author(String name, String books) {
        this.id = -1;
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
}
