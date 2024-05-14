package lat.jack.etl.utils;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class OracleDatabaseManager {

    public void resetDatabase(Connection connection) {
        try (connection) {
            connection.setAutoCommit(false);
            try (Statement stmt = connection.createStatement()) {

                // Dropping constraints and tables
                dropConstraints(stmt);
                dropTables(stmt);
                dropSequences(stmt);

                // Recreate sequences, tables, and constraints
                createSequences(stmt);
                createTables(stmt);
                createConstraints(stmt);

                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                e.printStackTrace();
                System.out.println("Error resetting staging database: " + e.getMessage());
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error obtaining connection: " + e.getMessage());
        }
    }

    private void dropConstraints(Statement stmt) throws SQLException {
        String[] dropConstraintStatements = {
                "ALTER TABLE FACTLOANEDBOOKS DROP CONSTRAINT FK_LOANED_BOOK_USER",
                "ALTER TABLE FACTLOANEDBOOKS DROP CONSTRAINT FK_LOANED_BOOK_BOOK",
                "ALTER TABLE FACTLOANEDBOOKS DROP CONSTRAINT FK_LOANED_BOOK_LOANED_AT_TIME",
                "ALTER TABLE FACTLOANEDBOOKS DROP CONSTRAINT FK_LOANED_BOOK_RETURNED_AT_TIME",
                "ALTER TABLE FACTLOANEDBOOKS DROP CONSTRAINT FK_LOANED_BOOK_PAID_AT_TIME",
                "ALTER TABLE DIMBOOKS DROP CONSTRAINT FK_BOOK_PUBLICATION_TIME",
                "ALTER TABLE DIMBOOKS DROP CONSTRAINT FK_BOOK_AUTHORS",
                "ALTER TABLE DIMBOOKS DROP CONSTRAINT FK_BOOK_CATEGORIES"
        };

        for (String sql : dropConstraintStatements) {
            stmt.execute(sql);
        }
    }

    private void dropTables(Statement stmt) throws SQLException {
        String[] dropTableStatements = {
                "DROP TABLE FACTLOANEDBOOKS",
                "DROP TABLE DIMBOOKS",
                "DROP TABLE DIMUSERS",
                "DROP TABLE DIMAUTHORS",
                "DROP TABLE DIMCATEGORIES",
                "DROP TABLE DIMTIME"
        };

        for (String sql : dropTableStatements) {
            stmt.execute(sql);
        }
    }

    private void dropSequences(Statement stmt) throws SQLException {
        String[] dropSequenceStatements = {
                "DROP SEQUENCE loaned_books_seq",
                "DROP SEQUENCE books_seq",
                "DROP SEQUENCE users_seq",
                "DROP SEQUENCE authors_seq",
                "DROP SEQUENCE categories_seq",
                "DROP SEQUENCE time_seq"
        };

        for (String sql : dropSequenceStatements) {
            stmt.execute(sql);
        }
    }

    private void createSequences(Statement stmt) throws SQLException {
        stmt.execute("CREATE SEQUENCE users_seq START WITH 1 INCREMENT BY 1");
        stmt.execute("CREATE SEQUENCE books_seq START WITH 1 INCREMENT BY 1");
        stmt.execute("CREATE SEQUENCE authors_seq START WITH 1 INCREMENT BY 1");
        stmt.execute("CREATE SEQUENCE categories_seq START WITH 1 INCREMENT BY 1");
        stmt.execute("CREATE SEQUENCE time_seq START WITH 1 INCREMENT BY 1");
        stmt.execute("CREATE SEQUENCE loaned_books_seq START WITH 1 INCREMENT BY 1");
    }

    private void createTables(Statement stmt) throws SQLException {
        stmt.execute(
                "CREATE TABLE DIMTIME (" +
                        "DATEID INTEGER DEFAULT TIME_SEQ.NEXTVAL PRIMARY KEY, " +
                        "DATE_TIME TIMESTAMP, " +
                        "MINUTE INTEGER, " +
                        "HOUR INTEGER, " +
                        "DAYOFWEEK INTEGER, " +
                        "MONTH INTEGER, " +
                        "QUARTER INTEGER, " +
                        "YEAR INTEGER, " +
                        "ISWEEKEND CHAR(1))"
        );

        stmt.execute(
                "CREATE TABLE DIMUSERS (" +
                        "USERID INTEGER DEFAULT USERS_SEQ.NEXTVAL PRIMARY KEY, " +
                        "USEREMAIL VARCHAR2(100), " +
                        "USERROLE VARCHAR2(50))"
        );

        stmt.execute(
                "CREATE TABLE DIMAUTHORS (" +
                        "AUTHORID INTEGER DEFAULT AUTHORS_SEQ.NEXTVAL PRIMARY KEY, " +
                        "AUTHORNAME VARCHAR2(100))"
        );

        stmt.execute(
                "CREATE TABLE DIMCATEGORIES (" +
                        "CATEGORYID INTEGER DEFAULT CATEGORIES_SEQ.NEXTVAL PRIMARY KEY, " +
                        "CATEGORYNAME VARCHAR2(100))"
        );

        stmt.execute(
                "CREATE TABLE DIMBOOKS (" +
                        "BOOKID INTEGER DEFAULT BOOKS_SEQ.NEXTVAL PRIMARY KEY, " +
                        "BOOKNAME VARCHAR2(255), " +
                        "BOOKISBN VARCHAR2(50), " +
                        "BOOKQUANTITY INTEGER, " +
                        "BOOKPUBLICATIONTIMEID INTEGER, " +
                        "BOOKAUTHORID INTEGER, " +
                        "BOOKCATEGORYID INTEGER)"
        );

        stmt.execute(
                "CREATE TABLE FACTLOANEDBOOKS (" +
                        "LOANEDBOOKSID INTEGER DEFAULT LOANED_BOOKS_SEQ.NEXTVAL PRIMARY KEY, " +
                        "USERID INTEGER, " +
                        "BOOKID INTEGER, " +
                        "LOANEDATTIMEID INTEGER, " +
                        "RETURNEDATTIMEID INTEGER, " +
                        "FINEAMOUNT DECIMAL(10, 2), " +
                        "PAIDATTIMEID INTEGER)"
        );
    }

    private void createConstraints(Statement stmt) throws SQLException {
        stmt.execute("ALTER TABLE DIMBOOKS ADD CONSTRAINT FK_BOOK_PUBLICATION_TIME FOREIGN KEY (BOOKPUBLICATIONTIMEID) REFERENCES DIMTIME(DATEID)");
        stmt.execute("ALTER TABLE DIMBOOKS ADD CONSTRAINT FK_BOOK_AUTHORS FOREIGN KEY (BOOKAUTHORID) REFERENCES DIMAUTHORS(AUTHORID)");
        stmt.execute("ALTER TABLE DIMBOOKS ADD CONSTRAINT FK_BOOK_CATEGORIES FOREIGN KEY (BOOKCATEGORYID) REFERENCES DIMCATEGORIES(CATEGORYID)");
        stmt.execute("ALTER TABLE FACTLOANEDBOOKS ADD CONSTRAINT FK_LOANED_BOOK_USER FOREIGN KEY (USERID) REFERENCES DIMUSERS(USERID)");
        stmt.execute("ALTER TABLE FACTLOANEDBOOKS ADD CONSTRAINT FK_LOANED_BOOK_BOOK FOREIGN KEY (BOOKID) REFERENCES DIMBOOKS(BOOKID)");
        stmt.execute("ALTER TABLE FACTLOANEDBOOKS ADD CONSTRAINT FK_LOANED_BOOK_LOANED_AT_TIME FOREIGN KEY (LOANEDATTIMEID) REFERENCES DIMTIME(DATEID)");
        stmt.execute("ALTER TABLE FACTLOANEDBOOKS ADD CONSTRAINT FK_LOANED_BOOK_RETURNED_AT_TIME FOREIGN KEY (RETURNEDATTIMEID) REFERENCES DIMTIME(DATEID)");
        stmt.execute("ALTER TABLE FACTLOANEDBOOKS ADD CONSTRAINT FK_LOANED_BOOK_PAID_AT_TIME FOREIGN KEY (PAIDATTIMEID) REFERENCES DIMTIME(DATEID)");
    }
}
