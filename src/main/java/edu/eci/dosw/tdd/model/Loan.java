package edu.eci.dosw.tdd.model;
import java.time.LocalDate;

public class Loan {

    private Book book;
    private User user;
    private LocalDate loanDate;
    private LoanStatus status;
    private LocalDate returnDate;

    public Loan(Book book, User user, LocalDate loanDate, LoanStatus status, LocalDate returnDate) {
        this.book = book;
        this.user = user;
        this.loanDate = loanDate;
        this.status = status;
        this.returnDate = returnDate;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    public LocalDate getLoanDate() {
        return loanDate;
    }
    public void setStatus(LoanStatus status) {
        this.status = status;
    }
    public LoanStatus getStatus() {
        return status;
    }
    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }
    public LocalDate getReturnDate() {
        return returnDate;
    }
    public void setLoanDate(LocalDate loanDate) {
        this.loanDate = loanDate;
    }
}