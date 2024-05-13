package lat.jack.etl.etl.extract.oracle.models;

import java.sql.Date;

public class OracleLoanFine {

    Integer id;
    Integer loanId;
    Integer fineAmount;
    Date paidAt;

    public OracleLoanFine(Integer id, Integer loanId, Integer fineAmount, Date paidAt) {
        this.id = id;
        this.loanId = loanId;
        this.fineAmount = fineAmount;
        this.paidAt = paidAt;
    }

    public OracleLoanFine() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getLoanId() {
        return loanId;
    }

    public void setLoanId(Integer loanId) {
        this.loanId = loanId;
    }

    public Integer getFineAmount() {
        return fineAmount;
    }

    public void setFineAmount(Integer fineAmount) {
        this.fineAmount = fineAmount;
    }

    public Date getPaidAt() {
        return paidAt;
    }

    public void setPaidAt(Date paidAt) {
        this.paidAt = paidAt;
    }
}
