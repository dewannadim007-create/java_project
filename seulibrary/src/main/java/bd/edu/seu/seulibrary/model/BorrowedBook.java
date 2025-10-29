package bd.edu.seu.seulibrary.model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity  public class BorrowedBook {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int borrowedBookId;

    private LocalDate borrowedDate;
    private LocalDate returnedDate;
    private boolean returned;
    private double fine;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne
    @JoinColumn(name = "bookId", nullable = false)
    private Book book;


    public int getBorrowedBookId() {
        return borrowedBookId;
    }


    public LocalDate getBorrowedDate() {
        return borrowedDate;
    }

    public void setBorrowedDate(LocalDate borrowedDate) {
        this.borrowedDate = borrowedDate;
    }

    public LocalDate getReturnedDate() {
        return returnedDate;
    }

    public void setReturnedDate(LocalDate returnedDate) {
        this.returnedDate = returnedDate;
    }

    public boolean isReturned() {
        return returned;
    }

    public void setReturned(boolean returned) {
        this.returned = returned;
    }

    public double getFine() {
        return fine;
    }

    public void setFine(double fine) {
        this.fine = fine;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }


}
