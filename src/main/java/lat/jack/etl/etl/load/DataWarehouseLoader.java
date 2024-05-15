package lat.jack.etl.etl.load;

import lat.jack.etl.models.*;
import lat.jack.etl.utils.InsertDimTime;
import lat.jack.etl.utils.OracleDBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;
import java.util.logging.Logger;

public class DataWarehouseLoader {

    private static final Logger logger = Logger.getLogger(DataWarehouseLoader.class.getName());
    private static final int BATCH_SIZE = 100;  // Prevents maximum open cursors (As unable to increase cursors in Oracle DB)

    public void loadAuthors(List<DimAuthor> authors) {
        String insertSQL = "INSERT INTO DIMAUTHORS (AUTHORID, AUTHORNAME) VALUES (?, ?)";
        try (Connection connection = OracleDBUtil.getDwDataSource();
             PreparedStatement statement = connection.prepareStatement(insertSQL)) {
            connection.setAutoCommit(false);
            int count = 0;

            for (DimAuthor author : authors) {
                statement.setInt(1, author.getAuthorId());
                statement.setString(2, author.getAuthorName());
                statement.addBatch();

                if (++count % BATCH_SIZE == 0) {
                    statement.executeBatch();
                }
            }
            statement.executeBatch(); // execute remaining batches
            connection.commit();
            logger.info("Authors loaded successfully into Data Warehouse");
        } catch (SQLException e) {
            logger.severe("Error loading authors: " + e.getMessage());
        }
    }

    public void loadCategories(List<DimCategory> categories) {
        String insertSQL = "INSERT INTO DIMCATEGORIES (CATEGORYID, CATEGORYNAME) VALUES (?, ?)";
        try (Connection connection = OracleDBUtil.getDwDataSource();
             PreparedStatement statement = connection.prepareStatement(insertSQL)) {
            connection.setAutoCommit(false);
            int count = 0;

            for (DimCategory category : categories) {
                statement.setInt(1, category.getCategoryId());
                statement.setString(2, category.getCategoryName());
                statement.addBatch();

                if (++count % BATCH_SIZE == 0) {
                    statement.executeBatch();
                }
            }
            statement.executeBatch(); // execute remaining batches
            connection.commit();
            logger.info("Categories loaded successfully into Data Warehouse");
        } catch (SQLException e) {
            logger.severe("Error loading categories: " + e.getMessage());
        }
    }

    public void loadUsers(List<DimUser> users) {
        String insertSQL = "INSERT INTO DIMUSERS (USERID, USEREMAIL, USERROLE) VALUES (?, ?, ?)";
        try (Connection connection = OracleDBUtil.getDwDataSource();
             PreparedStatement statement = connection.prepareStatement(insertSQL)) {
            connection.setAutoCommit(false);
            int count = 0;

            for (DimUser user : users) {
                statement.setInt(1, user.getUserId());
                statement.setString(2, user.getUserEmail());
                statement.setString(3, user.getUserRole());
                statement.addBatch();

                if (++count % BATCH_SIZE == 0) {
                    statement.executeBatch();
                }
            }
            statement.executeBatch(); // execute remaining batches
            connection.commit();
            logger.info("Users loaded successfully into Data Warehouse");
        } catch (SQLException e) {
            logger.severe("Error loading users: " + e.getMessage());
        }
    }

    public void loadBooks(List<DimBook> books) {
        String insertSQL = "INSERT INTO DIMBOOKS (BOOKID, BOOKNAME, BOOKISBN, BOOKQUANTITY, BOOKPUBLICATIONTIMEID, BOOKAUTHORID, BOOKCATEGORYID) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = OracleDBUtil.getDwDataSource();
             PreparedStatement statement = connection.prepareStatement(insertSQL)) {
            connection.setAutoCommit(false);
            int count = 0;

            for (DimBook book : books) {
                DimTime publicationTime = InsertDimTime.insertDimTime(connection, book.getPublicationTime());
                statement.setInt(1, book.getBookId());
                statement.setString(2, book.getBookName());
                statement.setString(3, book.getBookISBN());
                statement.setInt(4, book.getBookQuantity());
                statement.setInt(5, publicationTime.getDateId());
                statement.setInt(6, book.getAuthorId());
                statement.setInt(7, book.getCategoryId());
                statement.addBatch();

                if (++count % BATCH_SIZE == 0) {
                    statement.executeBatch();
                }
            }
            statement.executeBatch(); // execute remaining batches
            connection.commit();
            logger.info("Books loaded successfully into Data Warehouse");
        } catch (SQLException e) {
            logger.severe("Error loading books: " + e.getMessage());
        }
    }

    public void loadLoanedBooks(List<FactLoanedBook> loanedBooks) {
        String insertSQL = "INSERT INTO FACTLOANEDBOOKS (LOANEDBOOKSID, USERID, BOOKID, LOANEDATTIMEID, RETURNEDATTIMEID, FINEAMOUNT, PAIDATTIMEID) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = OracleDBUtil.getDwDataSource();
             PreparedStatement statement = connection.prepareStatement(insertSQL)) {
            connection.setAutoCommit(false);
            int count = 0;

            for (FactLoanedBook loanedBook : loanedBooks) {
                DimTime loanedTime = InsertDimTime.insertDimTime(connection, loanedBook.getLoanedTime());
                statement.setInt(1, loanedBook.getTransactionId());
                statement.setInt(2, loanedBook.getUserId());
                statement.setInt(3, loanedBook.getBookId());
                statement.setInt(4, loanedTime.getDateId());

                if (loanedBook.getReturnedTime() != null) {
                    DimTime returnedTime = InsertDimTime.insertDimTime(connection, loanedBook.getReturnedTime());
                    statement.setInt(5, returnedTime.getDateId());
                } else {
                    statement.setNull(5, Types.INTEGER);
                }

                statement.setDouble(6, loanedBook.getFineAmount());

                if (loanedBook.getPaidAtTime() != null) {
                    DimTime paidAtTime = InsertDimTime.insertDimTime(connection, loanedBook.getPaidAtTime());
                    statement.setInt(7, paidAtTime.getDateId());
                } else {
                    statement.setNull(7, Types.INTEGER);
                }

                statement.addBatch();

                if (++count % BATCH_SIZE == 0) {
                    statement.executeBatch();
                }
            }
            statement.executeBatch(); // execute remaining batches
            connection.commit();
            logger.info("Loaned books loaded successfully into Data Warehouse");
        } catch (SQLException e) {
            logger.severe("Error loading loaned books: " + e.getMessage());
        }
    }
}