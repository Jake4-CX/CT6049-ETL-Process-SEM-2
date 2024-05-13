package lat.jack.etl.etl.extract.oracle.service;

import lat.jack.etl.etl.extract.oracle.models.*;
import lat.jack.etl.utils.OracleDBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class OracleDB {

    private static final Logger logger = Logger.getLogger(OracleDB.class.getName());

    public OracleDB() {
    }

    public List<OracleUser> getUsers() {
        List<OracleUser> users = new ArrayList<>();
        String query = "SELECT * FROM USERS";
        try (Connection connection = OracleDBUtil.getOpDataSource()) {
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                OracleUser user = new OracleUser(
                        resultSet.getInt("ID"),
                        resultSet.getString("USEREMAIL"),
                        resultSet.getString("USERPASSWORD"),
                        resultSet.getDate("USERCREATEDDATE"),
                        resultSet.getDate("USERUPDATEDDATE"),
                        resultSet.getString("USERROLE")
                );
                users.add(user);
            }
            return users;
        } catch (SQLException e) {
            logger.severe("Error fetching users: " + e.getMessage());
        }
        return users; // Returns empty list if there's an exception
    }

    public List<OracleBookAuthor> getBookAuthors() {
        List<OracleBookAuthor> authors = new ArrayList<>();
        String query = "SELECT * FROM BOOKAUTHORS";
        try (Connection connection = OracleDBUtil.getOpDataSource()) {
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                authors.add(new OracleBookAuthor(
                        resultSet.getInt("ID"),
                        resultSet.getString("AUTHORFIRSTNAME"),
                        resultSet.getString("AUTHORLASTNAME")
                ));
            }
        } catch (SQLException e) {
            logger.severe("Error fetching book authors: " + e.getMessage());
        }
        return authors;
    }

    public List<OracleBookCategory> getBookCategories() {
        List<OracleBookCategory> categories = new ArrayList<>();
        String query = "SELECT * FROM BOOKCATEGORIES";
        try (Connection connection = OracleDBUtil.getOpDataSource()) {
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                categories.add(new OracleBookCategory(
                        resultSet.getInt("ID"),
                        resultSet.getString("CATEGORYNAME"),
                        resultSet.getString("CATEGORYDESCRIPTION")
                ));
            }
        } catch (SQLException e) {
            logger.severe("Error fetching book categories: " + e.getMessage());
        }
        return categories;
    }

    public List<OracleBook> getBooks() {
        List<OracleBook> books = new ArrayList<>();
        String query = "SELECT * FROM BOOKS";
        try (Connection connection = OracleDBUtil.getOpDataSource()) {
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                books.add(new OracleBook(
                        resultSet.getInt("ID"),
                        resultSet.getString("BOOKNAME"),
                        resultSet.getString("BOOKISBN"),
                        resultSet.getString("BOOKDESCRIPTION"),
                        resultSet.getInt("BOOKQUANTITY"),
                        resultSet.getString("BOOKTHUMBNAILURL"),
                        resultSet.getDate("BOOKPUBLISHEDDATE"),
                        resultSet.getInt("BOOKAUTHORID"),
                        resultSet.getInt("BOOKCATEGORYID")
                ));
            }
        } catch (SQLException e) {
            logger.severe("Error fetching books: " + e.getMessage());
        }
        return books;
    }

    // Implement similar methods for LoanedBooks, RefreshTokens, and LoanFines
    // Example for LoanedBooks
    public List<OracleLoanedBook> getLoanedBooks() {
        List<OracleLoanedBook> loanedBooks = new ArrayList<>();
        String query = "SELECT * FROM LOANEDBOOKS";
        try (Connection connection = OracleDBUtil.getOpDataSource()) {
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                loanedBooks.add(new OracleLoanedBook(
                        resultSet.getInt("ID"),
                        resultSet.getInt("BOOKID"),
                        resultSet.getInt("USERID"),
                        resultSet.getDate("LOANEDAT"),
                        resultSet.getDate("RETURNEDAT")
                ));
            }
        } catch (SQLException e) {
            logger.severe("Error fetching loaned books: " + e.getMessage());
        }
        return loanedBooks;
    }

    public List<OracleLoanFine> getLoanFines() {
        List<OracleLoanFine> loanFines = new ArrayList<>();
        String query = "SELECT * FROM LOANFINES";
        try (Connection connection = OracleDBUtil.getOpDataSource()) {
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                loanFines.add(new OracleLoanFine(
                        resultSet.getInt("ID"),
                        resultSet.getInt("LOANID"),
                        resultSet.getInt("FINEAMOUNT"),
                        resultSet.getDate("PAIDAT")
                ));
            }
        } catch (SQLException e) {
            logger.severe("Error fetching loan fines: " + e.getMessage());
        }
        return loanFines;
    }

}
