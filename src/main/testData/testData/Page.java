package hiof.gruppe1.testData;

public class Page {
    private int id;
    private int pageNr;
    private String text;

    public Page() {
    }

    public Page(int pageNr, String text) {
        this.pageNr = pageNr;
        this.text = text;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPageNr() {
        return pageNr;
    }

    public void setPageNr(int pageNr) {
        this.pageNr = pageNr;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
