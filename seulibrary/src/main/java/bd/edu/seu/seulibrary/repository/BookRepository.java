package bd.edu.seu.seulibrary.repository;

import bd.edu.seu.seulibrary.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository public interface BookRepository extends JpaRepository<Book, Integer> {

    List<Book> findByTitleContainingOrAuthorContaining(String title, String author);
}
