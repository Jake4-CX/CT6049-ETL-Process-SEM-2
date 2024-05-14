package lat.jack.etl.models;

public class DimBook {

    private int bookId;
    private String bookName;
    private String bookISBN;
    private int bookQuantity;
    private DimTime publicationTime;
    private int authorId;
    private int categoryId;

    public DimBook(int bookId, String bookName, String bookISBN, int bookQuantity, DimTime publicationTime, int authorId, int categoryId) {
        this.bookId = bookId;
        this.bookName = bookName;
        this.bookISBN = bookISBN;
        this.bookQuantity = bookQuantity;
        this.publicationTime = publicationTime;
        this.authorId = authorId;
        this.categoryId = categoryId;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getBookISBN() {
        return bookISBN;
    }

    public void setBookISBN(String bookISBN) {
        this.bookISBN = bookISBN;
    }

    public int getBookQuantity() {
        return bookQuantity;
    }

    public void setBookQuantity(int bookQuantity) {
        this.bookQuantity = bookQuantity;
    }

    public DimTime getPublicationTime() {
        return publicationTime;
    }

    public void setPublicationTime(DimTime publicationTime) {
        this.publicationTime = publicationTime;
    }

    public int getAuthorId() {
        return authorId;
    }

    public void setAuthorId(int authorId) {
        this.authorId = authorId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }
}
