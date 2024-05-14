package lat.jack.etl.models;

public class DimAuthor {

    private int authorId;
    private String authorName;

    public DimAuthor(int authorId, String authorName) {
        this.authorId = authorId;
        this.authorName = authorName;
    }

    public int getAuthorId() {
        return authorId;
    }

    public void setAuthorId(int authorId) {
        this.authorId = authorId;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }
}
