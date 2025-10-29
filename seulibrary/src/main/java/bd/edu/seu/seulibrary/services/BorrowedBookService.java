package bd.edu.seu.seulibrary.services;

import bd.edu.seu.seulibrary.model.Book;
import bd.edu.seu.seulibrary.model.BorrowedBook;
import bd.edu.seu.seulibrary.model.Member;
import bd.edu.seu.seulibrary.repository.BookRepository;
import bd.edu.seu.seulibrary.repository.BorrowedBookRepository;
import bd.edu.seu.seulibrary.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class BorrowedBookService {
    @Autowired
    private BorrowedBookRepository borrowedBookRepository;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private MemberRepository memberRepository;

    public BorrowedBook saveBorrowedBook(BorrowedBook borrowedBook) {
        return borrowedBookRepository.save(borrowedBook);
    }

    public BorrowedBook borrowBook(int memberId, int bookId) {
        Member member = memberRepository.findById(memberId).orElse(null);
        Book book = bookRepository.findById(bookId).orElse(null);

        if (member == null || book == null || book.getAvailableCopies() <= 0) {
            return null; // Cannot borrow if book is unavailable or member/book doesn't exist
        }

        // Check if the member has already borrowed this book and hasn't returned it
        BorrowedBook existingBorrowedBook = borrowedBookRepository.findByMemberAndBookAndReturned(member, book, false);
        if (existingBorrowedBook != null) {
            return null; // Member cannot borrow the same book again before returning the previous one
        }

        // Proceed to borrow the book if no conflict
        BorrowedBook borrowedBook = new BorrowedBook();
        borrowedBook.setMember(member);
        borrowedBook.setBook(book);
        borrowedBook.setBorrowedDate(LocalDate.now());
        borrowedBook.setReturned(false);

        book.setAvailableCopies(book.getAvailableCopies() - 1);
        bookRepository.save(book);

        return borrowedBookRepository.save(borrowedBook);
    }

    public BorrowedBook returnBook(int borrowedBookId) {
        BorrowedBook borrowedBook = borrowedBookRepository.findById(borrowedBookId).orElse(null);

        if (borrowedBook == null || borrowedBook.isReturned()) {
            return null;
        }

        borrowedBook.setReturned(true);
        borrowedBook.setReturnedDate(LocalDate.now());

        int daysBetween = (int) ChronoUnit.DAYS.between(borrowedBook.getBorrowedDate(), LocalDate.now());

        if (daysBetween > 7) {
            borrowedBook.setFine((daysBetween - 7) * 10);
        } else {
            borrowedBook.setFine(0);
        }

        Book book = borrowedBook.getBook();
        book.setAvailableCopies(book.getAvailableCopies() + 1);
        bookRepository.save(book);

        return borrowedBookRepository.save(borrowedBook);
    }

    public List<BorrowedBook> getAllBorrowedBooks() {
        return borrowedBookRepository.findAll();
    }
    public List<BorrowedBook> getBorrowedBooksByMemberId(int memberId) { return borrowedBookRepository.findByMember_MemberId(memberId); }

    public BorrowedBook getBorrowedBookById(int borrowedBookId) {
        return borrowedBookRepository.findById(borrowedBookId).orElse(null);
    }


    public void deleteById(int borrowedBookId) {
        borrowedBookRepository.deleteById(borrowedBookId);
    }
}

