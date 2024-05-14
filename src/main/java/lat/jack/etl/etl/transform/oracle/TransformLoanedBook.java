package lat.jack.etl.etl.transform.oracle;

import lat.jack.etl.etl.extract.oracle.models.OracleBook;
import lat.jack.etl.etl.extract.oracle.models.OracleLoanFine;
import lat.jack.etl.etl.extract.oracle.models.OracleLoanedBook;
import lat.jack.etl.etl.extract.oracle.models.OracleUser;
import lat.jack.etl.models.DimBook;
import lat.jack.etl.models.DimTime;
import lat.jack.etl.models.DimUser;
import lat.jack.etl.models.FactLoanedBook;
import lat.jack.etl.utils.InsertDimTime;
import lat.jack.etl.utils.OracleDBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

public class TransformLoanedBook {

    private static final Logger logger = Logger.getLogger(TransformLoanedBook.class.getName());
    private final List<FactLoanedBook> loanedBookCache = new ArrayList<>();

    public List<FactLoanedBook> transformLoanedBooks(List<OracleLoanedBook> loanedBooks, List<OracleLoanFine> loanFines, List<DimUser> users, List<OracleUser> oracleUsers, List<DimBook> books, List<OracleBook> oracleBooks) {
        try (Connection connection = OracleDBUtil.getStgDataSource()) {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM FACTLOANEDBOOKS");
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                int loanedBookId = rs.getInt("LOANEDBOOKSID");
                int userId = rs.getInt("USERID");
                int bookId = rs.getInt("BOOKID");
                int loanedAtDateId = rs.getInt("LOANEDATTIMEID");
                int returnedAtDateId = rs.getInt("RETURNEDATTIMEID");
                double fineAmount = rs.getDouble("FINEAMOUNT");
                int paidAtDateId = rs.getInt("PAIDATTIMEID");

                loanedBookCache.add(new FactLoanedBook(loanedBookId, bookId, userId, new DimTime(loanedAtDateId), new DimTime(returnedAtDateId), fineAmount, new DimTime(paidAtDateId)));
            }

        } catch (SQLException e) {
            logger.warning("Failed to connect to Oracle Database: " + e.getMessage());
        }

        for (OracleLoanedBook loanedBook : loanedBooks) {
            OracleLoanFine loanFine = loanFines.stream()
                    .filter(lf -> Objects.equals(lf.getLoanId(), loanedBook.getId()))
                    .findFirst()
                    .orElse(null);

            verifyLoanedBook(loanedBook, loanFine, users, oracleUsers, books, oracleBooks);
        }

        return loanedBookCache;
    }

    public void verifyLoanedBook(OracleLoanedBook loanedBook, OracleLoanFine loanFine, List<DimUser> users, List<OracleUser> oracleUsers, List<DimBook> books, List<OracleBook> oracleBooks) {

        int dimUserId = oracleUsers.stream()
                .filter(u -> Objects.equals(u.getId(), loanedBook.getUserId()))
                .findFirst()
                .map(u -> {
                    return users.stream()
                            .filter(du -> du.getUserEmail().equals(u.getUserEmail()))
                            .findFirst()
                            .map(DimUser::getUserId)
                            .orElse(-1);
                }).orElse(-1);

        if (dimUserId == -1) {
            logger.info("User not found for loaned book: " + loanedBook);
            return;
        }

        int dimBookId = oracleBooks.stream()
                .filter(b -> Objects.equals(b.getId(), loanedBook.getBookId()))
                .findFirst()
                .map(b -> {
                    return books.stream()
                            .filter(db -> db.getBookISBN().equals(b.getBookISBN()))
                            .findFirst()
                            .map(DimBook::getBookId)
                            .orElse(-1);
                }).orElse(-1);

        if (dimBookId == -1) {
            logger.info("Book not found for loaned book: " + loanedBook);
            return;
        }

        loanedBook.setUserId(dimUserId);
        loanedBook.setBookId(dimBookId);

        insertLoanedBook(loanedBook, loanFine);
    }

    public void insertLoanedBook(OracleLoanedBook loanedBook, OracleLoanFine loanFine) {
        int loanedBookId;
        int userId = loanedBook.getUserId();
        int bookId = loanedBook.getBookId();
        double fineAmount = loanFine != null ? loanFine.getFineAmount() : 0;

        if (loanedBookCache.stream().noneMatch(lb -> lb.getUserId() == userId && lb.getBookId() == bookId)) {
            try (Connection connection = OracleDBUtil.getStgDataSource()) {
                int loanedAtDateId = InsertDimTime.insertDimTime(connection, loanedBook.getLoanedAt());

                PreparedStatement statement = connection.prepareStatement("INSERT INTO FACTLOANEDBOOKS (USERID, BOOKID, LOANEDATTIMEID, RETURNEDATTIMEID, FINEAMOUNT, PAIDATTIMEID) VALUES (?, ?, ?, ?, ?, ?)", new String[] {"LOANEDBOOKSID"});
                statement.setInt(1, userId);
                statement.setInt(2, bookId);
                statement.setInt(3, loanedAtDateId);

                int returnedAtDateId = 0;
                if (loanedBook.getReturnedAt() != null) {
                    returnedAtDateId =  InsertDimTime.insertDimTime(connection, loanedBook.getReturnedAt());
                    statement.setInt(4, returnedAtDateId);
                } else {
                    statement.setNull(4, Types.INTEGER);
                }

                statement.setDouble(5, fineAmount);

                int paidAtDateId = 0;
                if (loanFine != null && loanFine.getPaidAt() != null) {
                    paidAtDateId = InsertDimTime.insertDimTime(connection, loanFine.getPaidAt());
                    statement.setInt(6, paidAtDateId);
                } else {
                    statement.setNull(6, Types.INTEGER);
                }

                int affectedRows = statement.executeUpdate();
                if (affectedRows == 0) {
                    throw new SQLException("Failed to insert FactLoanedBook record.");
                }

                try (ResultSet resultSet = statement.getGeneratedKeys()) {
                    if (resultSet.next()) {
                        loanedBookId = resultSet.getInt(1);
                        if (loanFine != null && paidAtDateId != 0) {
                            loanedBookCache.add(new FactLoanedBook(loanedBookId, bookId, userId, new DimTime(loanedAtDateId), (returnedAtDateId == 0 ? new DimTime(returnedAtDateId) : null), fineAmount, new DimTime(paidAtDateId)));
                        } else {
                            loanedBookCache.add(new FactLoanedBook(loanedBookId, bookId, userId, new DimTime(loanedAtDateId), (returnedAtDateId == 0 ? new DimTime(returnedAtDateId) : null), fineAmount, null));
                        }
                    } else {
                        throw new SQLException("Failed to insert FactLoanedBook record.");
                    }
                }
            } catch (SQLException e) {
                logger.warning("Oracle Database Error: " + e.getMessage());
            }
        }
    }
}
