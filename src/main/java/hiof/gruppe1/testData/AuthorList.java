package hiof.gruppe1.testData;

public class AuthorList {
    private int id;
    private String name;
    Author topAuthor;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public AuthorList() {
    }

    public Author getTopAuthor() {
        return topAuthor;
    }

    public void setTopAuthor(Author topAuthor) {
        this.topAuthor = topAuthor;
    }
}
