package lat.jack.etl.etl.extract.oracle.models;

import java.sql.Date;

public class OracleLoanedBook {

    Integer id;
    Integer bookId;
    Integer userId;
    Date loanedAt;
    Date returnedAt;

    public OracleLoanedBook(Integer id, Integer bookId, Integer userId, Date loanedAt, Date returnedAt) {
        this.id = id;
        this.bookId = bookId;
        this.userId = userId;
        this.loanedAt = loanedAt;
        this.returnedAt = returnedAt;
    }

    public OracleLoanedBook() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getBookId() {
        return bookId;
    }

    public void setBookId(Integer bookId) {
        this.bookId = bookId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Date getLoanedAt() {
        return loanedAt;
    }

    public void setLoanedAt(Date loanedAt) {
        this.loanedAt = loanedAt;
    }

    public Date getReturnedAt() {
        return returnedAt;
    }

    public void setReturnedAt(Date returnedAt) {
        this.returnedAt = returnedAt;
    }
}
