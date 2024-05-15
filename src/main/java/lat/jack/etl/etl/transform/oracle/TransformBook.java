package lat.jack.etl.etl.transform.oracle;

import lat.jack.etl.etl.extract.oracle.models.OracleBook;
import lat.jack.etl.etl.extract.oracle.models.OracleBookAuthor;
import lat.jack.etl.etl.extract.oracle.models.OracleBookCategory;
import lat.jack.etl.models.DimAuthor;
import lat.jack.etl.models.DimBook;
import lat.jack.etl.models.DimCategory;
import lat.jack.etl.models.DimTime;
import lat.jack.etl.utils.InsertDimTime;
import lat.jack.etl.utils.OracleDBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class TransformBook {

    private static final Logger logger = Logger.getLogger(TransformBook.class.getName());
    private final List<DimBook> bookCache = new ArrayList<>();

    public List<DimBook> transformBooks(List<OracleBook> books, List<DimAuthor> authors, List<OracleBookAuthor> oracleAuthors, List<DimCategory> categories, List<OracleBookCategory> oracleCategories) {

        try (Connection connection = OracleDBUtil.getStgDataSource()) {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM DIMBOOKS");
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                int bookId = rs.getInt("BOOKID");
                String bookTitle = rs.getString("BOOKNAME");
                String bookISBN = rs.getString("BOOKISBN");
                int bookQuantity = rs.getInt("BOOKQUANTITY");
                int publicationTimeId = rs.getInt("BOOKPUBLICATIONTIMEID");
                int bookAuthorId = rs.getInt("BOOKAUTHORID");
                int bookCategoryId = rs.getInt("BOOKCATEGORYID");

                bookCache.add(new DimBook(bookId, bookTitle, bookISBN, bookQuantity, new DimTime(publicationTimeId), bookAuthorId, bookCategoryId));
            }

        } catch (SQLException e) {
            logger.warning("Failed to connect to Oracle Database: " + e.getMessage());
            return null;
        }

        for (OracleBook book : books) {
            verifyBook(book, authors, oracleAuthors, categories, oracleCategories);
        }

        return bookCache;
    }

    public void verifyBook(OracleBook book, List<DimAuthor> authors, List<OracleBookAuthor> oracleAuthors, List<DimCategory> categories, List<OracleBookCategory> oracleCategories) {
        logger.info("Current system book count: " + bookCache.size());
        if (bookCache.stream().anyMatch(b -> b.getBookName().equalsIgnoreCase(book.getBookName()))) {
            logger.info("Skipping duplicate book entry: " + book.getBookName());
            return; // (book already exists)
        }

        // Find the correct author ID
        int dimAuthorId = oracleAuthors.stream()
                .filter(oa -> oa.getId().equals(book.getBookAuthorId()))
                .findFirst()
                .map(oa -> {
                    String fullAuthorName = oa.getAuthorFirstName() + " " + oa.getAuthorLastName();
                    return authors.stream()
                            .filter(a -> a.getAuthorName().equals(fullAuthorName))
                            .findFirst()
                            .map(DimAuthor::getAuthorId)
                            .orElse(-1);
                }).orElse(-1);

        if (dimAuthorId == -1) {
            logger.info("Author not found for book: " + book.getBookName());
            return;
        }

        // Find the correct category ID
        int dimCategoryId = oracleCategories.stream()
                .filter(oc -> oc.getId().equals(book.getBookCategoryId()))
                .findFirst()
                .map(oc -> categories.stream()
                        .filter(c -> c.getCategoryName().equals(oc.getCategoryName()))
                        .findFirst()
                        .map(DimCategory::getCategoryId)
                        .orElse(-1))
                .orElse(-1);

        if (dimCategoryId == -1) {
            logger.info("Category not found for book: " + book.getBookName());
            return;
        }

        book.setBookAuthorId(dimAuthorId);
        book.setBookCategoryId(dimCategoryId);

        logger.info("Inserting book: " + book.getBookName());
        insertBook(book);

    }

    public void insertBook(OracleBook book) {
        int bookId;
        String bookTitle = book.getBookName();
        String bookISBN = book.getBookISBN();
        int bookQuantity = book.getBookQuantity();
        int bookAuthorId = book.getBookAuthorId();
        int bookCategoryId = book.getBookCategoryId();
        Date bookPublishedDate = book.getBookPublishedDate();

        if (bookCache.stream().noneMatch(b -> b.getBookName().equals(bookTitle))) {
            try (Connection connection = OracleDBUtil.getStgDataSource()) {
                DimTime publicationTime = InsertDimTime.insertDimTime(connection, bookPublishedDate);

                PreparedStatement statement = connection.prepareStatement("INSERT INTO DIMBOOKS (BOOKNAME, BOOKISBN, BOOKQUANTITY, BOOKPUBLICATIONTIMEID, BOOKAUTHORID, BOOKCATEGORYID) VALUES (?, ?, ?, ?, ?, ?)", new String[] {"BOOKID"});
                statement.setString(1, bookTitle);
                statement.setString(2, bookISBN);
                statement.setInt(3, bookQuantity);
                statement.setInt(4, publicationTime.getDateId());
                statement.setInt(5, bookAuthorId);
                statement.setInt(6, bookCategoryId);

                int affectedRows = statement.executeUpdate();
                if (affectedRows == 0) {
                    throw new SQLException("Failed to insert DimBook record.");
                }

                try (ResultSet resultSet = statement.getGeneratedKeys()) {
                    if (resultSet.next()) {
                        bookId = resultSet.getInt(1);
                        bookCache.add(new DimBook(bookId, bookTitle, bookISBN, bookQuantity, publicationTime, bookAuthorId, bookCategoryId));
                    } else {
                        throw new SQLException("Failed to insert DimBook record.");
                    }
                }

            } catch (SQLException e) {
                logger.warning("Oracle Database Error: " + e.getMessage());
            }
        }
    }
}
