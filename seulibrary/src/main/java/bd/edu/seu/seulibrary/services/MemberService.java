package bd.edu.seu.seulibrary.services;
import bd.edu.seu.seulibrary.model.BorrowedBook;
import bd.edu.seu.seulibrary.model.Member;
import bd.edu.seu.seulibrary.repository.BorrowedBookRepository;
import bd.edu.seu.seulibrary.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemberService {
    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Autowired
    private BorrowedBookRepository borrowedBookRepository;

    public Member saveMember(Member member) {
        return memberRepository.save(member);
    }

    public List<Member> getAllMembers() {
        return memberRepository.findAll();
    }

    public Member getMemberById(int id) {
        return memberRepository.findById(id).orElse(null);
    }
    public List<Member >getMemberByName(String name) {
        return memberRepository.findByName(name);
    }

    public Member getMemberByIdAndPassword(int memberId, String password) {
        return memberRepository.findByMemberIdAndPassword(memberId, password);
    }

    public List<BorrowedBook> getBorrowedBooksByMemberId(int memberId) {
        return borrowedBookRepository.findByMember_MemberId(memberId);
    }
    public Member authenticateMember(int memberId, String password) {
        return memberRepository.findByMemberIdAndPassword(memberId, password);
    }

    public void deleteMember(int memberId) {
        memberRepository.deleteById(memberId);
    }

}