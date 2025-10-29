package bd.edu.seu.seulibrary.controller;

import bd.edu.seu.seulibrary.services.MemberService;
import bd.edu.seu.seulibrary.services.BookService;
import bd.edu.seu.seulibrary.services.BorrowedBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.SessionAttributes;


@Controller
@SessionAttributes("loggedInMember")
public class MemberController {

    @Autowired
    private MemberService memberService;

    @Autowired
    private BookService bookService;

    @Autowired
    private BorrowedBookService borrowedBookService;

}
