package lat.jack.etl.models;

import java.util.List;

public class TransformedCollection {

    List<DimAuthor> authors;
    List<DimCategory> categories;
    List<DimUser> users;
    List<DimBook> books;
    List<FactLoanedBook> loanedBooks;

    public TransformedCollection(List<DimAuthor> authors, List<DimCategory> categories, List<DimUser> users, List<DimBook> books, List<FactLoanedBook> loanedBooks) {
        this.authors = authors;
        this.categories = categories;
        this.users = users;
        this.books = books;
        this.loanedBooks = loanedBooks;
    }

    public TransformedCollection() {
    }

    public List<DimAuthor> getAuthors() {
        return authors;
    }

    public void setAuthors(List<DimAuthor> authors) {
        this.authors = authors;
    }

    public List<DimCategory> getCategories() {
        return categories;
    }

    public void setCategories(List<DimCategory> categories) {
        this.categories = categories;
    }

    public List<DimUser> getUsers() {
        return users;
    }

    public void setUsers(List<DimUser> users) {
        this.users = users;
    }

    public List<DimBook> getBooks() {
        return books;
    }

    public void setBooks(List<DimBook> books) {
        this.books = books;
    }

    public List<FactLoanedBook> getLoanedBooks() {
        return loanedBooks;
    }

    public void setLoanedBooks(List<FactLoanedBook> loanedBooks) {
        this.loanedBooks = loanedBooks;
    }
}
