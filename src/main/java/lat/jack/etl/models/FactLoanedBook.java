package lat.jack.etl.models;

public class FactLoanedBook {

    private int transactionId;
    private int userId;
    private int bookId;
    private DimTime loanedTime;
    private DimTime returnedTime;
    private double fineAmount;
    private DimTime paidAtTime;

    public FactLoanedBook(int transactionId, int userId, int bookId, DimTime loanedTime, DimTime returnedTime, double fineAmount, DimTime paidAtTime) {
        this.transactionId = transactionId;
        this.userId = userId;
        this.bookId = bookId;
        this.loanedTime = loanedTime;
        this.returnedTime = returnedTime;
        this.fineAmount = fineAmount;
        this.paidAtTime = paidAtTime;
    }

    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public DimTime getLoanedTime() {
        return loanedTime;
    }

    public void setLoanedTime(DimTime loanedTime) {
        this.loanedTime = loanedTime;
    }

    public DimTime getReturnedTime() {
        return returnedTime;
    }

    public void setReturnedTime(DimTime returnedTime) {
        this.returnedTime = returnedTime;
    }

    public double getFineAmount() {
        return fineAmount;
    }

    public void setFineAmount(double fineAmount) {
        this.fineAmount = fineAmount;
    }

    public DimTime getPaidAtTime() {
        return paidAtTime;
    }

    public void setPaidAtTime(DimTime paidAtTime) {
        this.paidAtTime = paidAtTime;
    }
}
