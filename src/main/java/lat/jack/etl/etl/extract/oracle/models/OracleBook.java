package lat.jack.etl.etl.extract.oracle.models;

import java.sql.Date;

public class OracleBook {

    Integer id;
    String bookName;
    String bookISBN;
    String bookDescription;
    Integer bookQuantity;
    String bookThumbnailURL;
    Date bookPublishedDate;
    Integer bookAuthorId;
    Integer bookCategoryId;

    public OracleBook(Integer id, String bookName, String bookISBN, String bookDescription, Integer bookQuantity, String bookThumbnailURL, Date bookPublishedDate, Integer bookAuthorId, Integer bookCategoryId) {
        this.id = id;
        this.bookName = bookName;
        this.bookISBN = bookISBN;
        this.bookDescription = bookDescription;
        this.bookQuantity = bookQuantity;
        this.bookThumbnailURL = bookThumbnailURL;
        this.bookPublishedDate = bookPublishedDate;
        this.bookAuthorId = bookAuthorId;
        this.bookCategoryId = bookCategoryId;
    }

    public OracleBook() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getBookDescription() {
        return bookDescription;
    }

    public void setBookDescription(String bookDescription) {
        this.bookDescription = bookDescription;
    }

    public Integer getBookQuantity() {
        return bookQuantity;
    }

    public void setBookQuantity(Integer bookQuantity) {
        this.bookQuantity = bookQuantity;
    }

    public String getBookThumbnailURL() {
        return bookThumbnailURL;
    }

    public void setBookThumbnailURL(String bookThumbnailURL) {
        this.bookThumbnailURL = bookThumbnailURL;
    }

    public Date getBookPublishedDate() {
        return bookPublishedDate;
    }

    public void setBookPublishedDate(Date bookPublishedDate) {
        this.bookPublishedDate = bookPublishedDate;
    }

    public Integer getBookAuthorId() {
        return bookAuthorId;
    }

    public void setBookAuthorId(Integer bookAuthorId) {
        this.bookAuthorId = bookAuthorId;
    }

    public Integer getBookCategoryId() {
        return bookCategoryId;
    }

    public void setBookCategoryId(Integer bookCategoryId) {
        this.bookCategoryId = bookCategoryId;
    }
}
