package lat.jack.etl.etl.extract.oracle.models;

import java.util.List;

public class OracleData {

    List<OracleBookAuthor> bookAuthors;
    List<OracleBookCategory> bookCategories;
    List<OracleBook> books;
    List<OracleUser> users;
    List<OracleLoanedBook> loanedBooks;
    List<OracleLoanFine> loanFines;

    public OracleData(List<OracleBookAuthor> bookAuthors, List<OracleBookCategory> bookCategories, List<OracleBook> books, List<OracleUser> users, List<OracleLoanedBook> loanedBooks, List<OracleLoanFine> loanFines) {
        this.bookAuthors = bookAuthors;
        this.bookCategories = bookCategories;
        this.books = books;
        this.users = users;
        this.loanedBooks = loanedBooks;
        this.loanFines = loanFines;
    }

    public OracleData() {
    }

    public List<OracleBookAuthor> getBookAuthors() {
        return bookAuthors;
    }

    public void setBookAuthors(List<OracleBookAuthor> bookAuthors) {
        this.bookAuthors = bookAuthors;
    }

    public List<OracleBookCategory> getBookCategories() {
        return bookCategories;
    }

    public void setBookCategories(List<OracleBookCategory> bookCategories) {
        this.bookCategories = bookCategories;
    }

    public List<OracleBook> getBooks() {
        return books;
    }

    public void setBooks(List<OracleBook> books) {
        this.books = books;
    }

    public List<OracleUser> getUsers() {
        return users;
    }

    public void setUsers(List<OracleUser> users) {
        this.users = users;
    }

    public List<OracleLoanedBook> getLoanedBooks() {
        return loanedBooks;
    }

    public void setLoanedBooks(List<OracleLoanedBook> loanedBooks) {
        this.loanedBooks = loanedBooks;
    }

    public List<OracleLoanFine> getLoanFines() {
        return loanFines;
    }

    public void setLoanFines(List<OracleLoanFine> loanFines) {
        this.loanFines = loanFines;
    }
}
