package bd.edu.seu.seulibrary.services;

import bd.edu.seu.seulibrary.model.Book;
import bd.edu.seu.seulibrary.repository.BookRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class BookService {
    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public Book saveBook(Book book) {
        return bookRepository.save(book);
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public Book getBookById(int id) {
        return bookRepository.findById(id).orElse(null);
    }

    public void deleteBook(int id) {
        bookRepository.deleteById(id);
    }
    public List<Book> findByTitleContainingOrAuthorContaining(String title, String author) {
        return bookRepository.findByTitleContainingOrAuthorContaining(title, author);
    }
}
