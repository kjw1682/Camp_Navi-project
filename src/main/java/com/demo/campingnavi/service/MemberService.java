package com.demo.campingnavi.service;

import com.demo.campingnavi.domain.Member;
import com.demo.campingnavi.domain.Recommend;
import com.demo.campingnavi.dto.MemberVo;
import jakarta.servlet.http.HttpSession;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface MemberService {

    Member findById(int mseq);

    Member findByUsername(String username);

    void saveMember(Member member);

    boolean joinProcess(MemberVo vo);

    boolean adminAdditionProc(MemberVo vo);

    boolean loginProcess(MemberVo vo);

    boolean isUserName(MemberVo vo);

    boolean isNickName(MemberVo vo);

    boolean isEmail(String email);

    boolean validatePw(String pw);

    boolean validateUsername(String username);

    String getUsername(String name, String email, String birth, String phone, String provider);

    List<String> getEmailList();

    boolean isMemberByPw(String name, String username, String email, String birth, String phone);

    Page<Recommend> getList(int page, Member member);

    Page<Member> findAll(Pageable pageable);

    Page<Member> findAllByUsername(String username, Pageable pageable);

    Page<Member> findAllByName(String name, Pageable pageable);

    Page<Member> findAllByProvider(String provider, Pageable pageable);

    Page<Member> findAllByEmail(String eamil, Pageable pageable);

    Page<Member> findAllAdmin(Pageable pageable);

    Page<Member> findAllAdminByUsername(String username, Pageable pageable);

    Page<Member> findAllAdminByName(String name, Pageable pageable);
}
