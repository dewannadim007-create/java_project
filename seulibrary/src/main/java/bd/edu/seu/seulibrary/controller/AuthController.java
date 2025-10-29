package bd.edu.seu.seulibrary.controller;
import bd.edu.seu.seulibrary.model.Book;
import bd.edu.seu.seulibrary.model.BorrowedBook;
import bd.edu.seu.seulibrary.model.Member;
import bd.edu.seu.seulibrary.model.Admin;
import bd.edu.seu.seulibrary.services.BookService;
import bd.edu.seu.seulibrary.services.BorrowedBookService;
import bd.edu.seu.seulibrary.services.MemberService;
import bd.edu.seu.seulibrary.services.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@SessionAttributes({"loggedInMember", "loggedInAdmin"})
public class AuthController {

    @Autowired
    private MemberService memberService;

    @Autowired
    private AdminService adminService;
    @Autowired
    private BookService bookService;
    @Autowired
    private BorrowedBookService borrowedBookService;

    @GetMapping("/")
    public String showLoginPage() {
        return "login";
    }
    @GetMapping("/member/home")
    public String showMemberHome(@ModelAttribute("loggedInMember") Member loggedInMember, Model model) {
        Member member = memberService.getMemberById(loggedInMember.getMemberId());
        model.addAttribute("member", member);
        List<Book> availableBooks = bookService.getAllBooks();
        List<BorrowedBook> borrowedBooks = borrowedBookService. getBorrowedBooksByMemberId(loggedInMember.getMemberId());

        model.addAttribute("availableBooks", availableBooks);
        model.addAttribute("borrowedBooks", borrowedBooks);

        return "member_home";
    }

    @PostMapping("/login")
    public String login(@RequestParam int memberId, @RequestParam String password, Model model) {

        Member member = memberService.authenticateMember(memberId, password);

        if (member != null) {
            model.addAttribute("loggedInMember", member);
            return "redirect:/member/home";
        }

        Admin admin = adminService.login(memberId, password);

        if (admin != null) {
            model.addAttribute("loggedInAdmin", admin);
            return "admin_home";
        }

        model.addAttribute("error", "Invalid ID or password!");
        return "login";
    }

    @GetMapping("/logout")
    public String logout(Model model) {
        model.addAttribute("loggedInMember", null);
        model.addAttribute("loggedInAdmin", null);
        return "redirect:/";
    }

    @GetMapping("/signup")
    public String showSignupPage() {
        return "signup";
    }


    @PostMapping("/signup")
    public String signUp(@RequestParam int memberId, @RequestParam String name, @RequestParam String email, @RequestParam String password, @RequestParam String mobile,@RequestParam String address,Model model) {

        Member existingMember = memberService.getMemberById(memberId);
        if (existingMember != null) {
            model.addAttribute("error", "A member with this ID already exists!");
            return "signup";
        }

        Member newMember = new Member();
        newMember.setMemberId(memberId);
        newMember.setName(name);
        newMember.setEmail(email);
        newMember.setPassword(password);
        newMember.setMobile(mobile);
        newMember.setAddress(address);
        memberService.saveMember(newMember);
        model.addAttribute("loggedInMember", newMember);
        return "login";
    }

    @GetMapping("/admin/home")
    public String showAdminHome(@RequestParam(name = "search", required = false) String search, Model model) {
        List<Book> books;

        if (search != null && !search.isEmpty()) {

            books = bookService.findByTitleContainingOrAuthorContaining(search, search);

        } else {
            books = bookService.getAllBooks();
        }
        model.addAttribute("books", books);
        return "admin_home";
    }

}
