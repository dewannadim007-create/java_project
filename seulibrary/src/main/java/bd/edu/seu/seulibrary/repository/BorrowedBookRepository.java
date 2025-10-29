package bd.edu.seu.seulibrary.repository;

import bd.edu.seu.seulibrary.model.Book;
import bd.edu.seu.seulibrary.model.BorrowedBook;
import bd.edu.seu.seulibrary.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BorrowedBookRepository extends JpaRepository<BorrowedBook, Integer> {
    List<BorrowedBook> findByMember_MemberId(int memberId);
        BorrowedBook findByMemberAndBookAndReturned(Member member, Book book, boolean returned);
}


