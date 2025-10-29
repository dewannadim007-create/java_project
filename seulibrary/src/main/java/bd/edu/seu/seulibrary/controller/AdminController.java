package bd.edu.seu.seulibrary.controller;

import bd.edu.seu.seulibrary.model.Book;
import bd.edu.seu.seulibrary.model.BorrowedBook;
import bd.edu.seu.seulibrary.model.Member;
import bd.edu.seu.seulibrary.services.BookService;
import bd.edu.seu.seulibrary.services.BorrowedBookService;
import bd.edu.seu.seulibrary.services.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
public class AdminController {

    @Autowired
    private MemberService memberService;
    @Autowired
    private BookService bookService;
    @Autowired
    private BorrowedBookService borrowedBookService;


    @GetMapping("/admin/admin_add_member")
    public String showAddMemberPage() {
        return "admin_add_member";
    }

    @PostMapping("/admin/admin_add_member")
    public String addMemberByAdmin(@RequestParam int memberId, @RequestParam String name, @RequestParam String email,
            @RequestParam String password,
            @RequestParam String phone,
            @RequestParam String address,
            Model model) {

        Member existingMember = memberService.getMemberById(memberId);
        if (existingMember != null) {
            model.addAttribute("error", "A member with this ID already exists!");
            return "admin_add_member";
        }
        Member newMember = new Member();
        newMember.setMemberId(memberId);
        newMember.setName(name);
        newMember.setEmail(email);
        newMember.setPassword(password);
        newMember.setMobile(phone);
        newMember.setAddress(address);

        memberService.saveMember(newMember);

        model.addAttribute("success", "Member added successfully!");
        return "admin_add_member";
    }

    @GetMapping("/admin/admin_add_book")
    public String showAddBookPage(Model model) {

        return "admin_add_book";
    }

    @PostMapping("/admin/admin_add_book")
    public String addBook(@RequestParam String title,
                           @RequestParam String isbn,
                           @RequestParam int totalCopies,
                           @RequestParam String author,
                           Model model) {
        Book newBook = new Book();
        newBook.setTitle(title);
        newBook.setIsbn(isbn);
        newBook.setTotalCopies(totalCopies);
        newBook.setAvailableCopies(totalCopies);
        newBook.setAuthor(author);

        bookService.saveBook(newBook);
        return "admin_add_book";
    }

    @GetMapping("/admin/memberList")
    public String showMemberList(@RequestParam(name = "search", required = false) String search , Model model) {
        List<Member> members;

        if (search != null ) {
            members = memberService.getMemberByName(search);
        } else {
            members = memberService.getAllMembers();
        }

        model.addAttribute("members", members);
        return "memberList";
    }
    @PostMapping("/admin/delete-member")
    public String deleteMember(@RequestParam("memberId") int memberId) {
        memberService.deleteMember(memberId);
        return "redirect:/admin/memberList";
    }

    @GetMapping("/admin/edit-member/{memberId}")
    public String editMember(@PathVariable int memberId, Model model) {
        Member member = memberService.getMemberById(memberId);
        model.addAttribute("member", member);
        return "edit-Member";
    }

    @GetMapping("/admin/edit-member")
    public String showEditMemberForm(@RequestParam("memberId") int memberId, Model model) {
        Member member = memberService.getMemberById(memberId);
        model.addAttribute("member", member);
        return "edit-Member";  }

    @PostMapping("/admin/save-member")
    public String saveEditedMember(@ModelAttribute Member member) {
        Member existingMember = memberService.getMemberById(member.getMemberId());

        if (member.getPassword() == null || member.getPassword().isEmpty()) {
            member.setPassword(existingMember.getPassword());
        }

        memberService.saveMember(member);
        return "redirect:/admin/memberList";
    }



    @GetMapping("/admin/admin_lend_book")
    public String showLendBookPage(Model model) {
        List<Book> availableBooks = bookService.getAllBooks();
        model.addAttribute("books", availableBooks);
        return "admin_lend_book";
    }


    @PostMapping("/admin/admin_lend_book")
    public String lendBook(@RequestParam("bookId") int bookId,
                           @RequestParam("memberId") int memberId,
                           @RequestParam("returnDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate returnDate,
                           Model model) {

        BorrowedBook borrowedBook = borrowedBookService.borrowBook(memberId, bookId);


        if (borrowedBook == null) {
            model.addAttribute("error", "The member has already borrowed this book and hasn't returned it yet.");
        } else {
            borrowedBook.setReturnedDate(returnDate);
            borrowedBookService.saveBorrowedBook(borrowedBook);
            model.addAttribute("success", "Book lent successfully!");
        }

        return "admin_lend_book";
    }

    @GetMapping("/admin/borrowedBookList")
    public String showBorrowedBookList(Model model) {
        List<BorrowedBook> borrowedBooks = borrowedBookService.getAllBorrowedBooks();
        model.addAttribute("borrowedBooks", borrowedBooks);
        return "borrowedBookList";
    }

    @PostMapping("/return_book/{borrowedBookId}")
    public String returnBook(@PathVariable int borrowedBookId) {

        BorrowedBook borrowedBook = borrowedBookService.getBorrowedBookById(borrowedBookId);
        borrowedBook.setReturned(true);
        Book book = borrowedBook.getBook();
        book.setAvailableCopies(book.getAvailableCopies() + 1);
        borrowedBookService.saveBorrowedBook(borrowedBook);
        bookService.saveBook(book);

        return "redirect:/admin/borrowedBookList";
    }

    @PostMapping("/delete_borrowed_book/{borrowedBookId}")
    public String deleteBorrowedBook(@PathVariable int borrowedBookId) {
        borrowedBookService.deleteById(borrowedBookId);
        return "redirect:/admin/borrowedBookList";
    }
}
