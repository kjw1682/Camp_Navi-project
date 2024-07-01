package com.demo.campingnavi.service;

import com.demo.campingnavi.domain.Member;
import com.demo.campingnavi.domain.Recommend;
import com.demo.campingnavi.domain.Role;
import com.demo.campingnavi.dto.MemberVo;
import com.demo.campingnavi.repository.jpa.MemberRepository;
import com.demo.campingnavi.repository.jpa.RecommendRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Service
public class MemberServiceImpl implements MemberService{

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RecommendRepository recommendRepository;

    @Override
    public Member findById(int mseq) {
        return memberRepository.findById(mseq).get();
    }

    @Override
    public Member findByUsername(String username) {
        return memberRepository.findByUsername(username);
    }

    @Override
    public void saveMember(Member member) {
        memberRepository.save(member);
    }

    @Override
    public boolean joinProcess(MemberVo vo) {
        boolean result = false;
        String PATTERN_ID = "^[a-z]{1}[a-z0-9]{5,20}+$";
        String PATTERN_PW = "^(?=.*[A-Z])(?=.*[a-z])(?=.*[!@#$%^&*(),.?\":{}|<>]).{8,20}+$";
        boolean idPattern = Pattern.matches(PATTERN_ID, vo.getUsername());
        boolean pwPattern = Pattern.matches(PATTERN_PW, vo.getPw());

        // 존재여부확인
        boolean isMember = memberRepository.existsByUsername(vo.getUsername());

        if(idPattern && pwPattern && !isMember){
            String encodedPassword = passwordEncoder.encode(vo.getPw());
            Member member = Member.builder()
                    .username(vo.getUsername())
                    .pw(encodedPassword)
                    .name(vo.getName())
                    .sex(vo.getSex())
                    .email(vo.getEmail())
                    .addr1(vo.getAddr1())
                    .addr2(" ")
                    .provider("campingnavi")
                    .providerId("campingnavi" + vo.getUsername())
                    .phone(vo.getPhone() + vo.getPhone2())
                    .nickname(vo.getNickname())
                    .birth(vo.getBirth())
                    .useyn("y")
                    .role(Role.USER.getKey())
                    .build();
            memberRepository.save(member);
            result = true;
            return result;
        } else {
            return result;
        }
    }

    @Override
    public boolean adminAdditionProc(MemberVo vo) {
        boolean result = false;
        boolean isMember = memberRepository.existsByUsername(vo.getUsername());
        if (!isMember) {
            Member admin = Member.builder()
                    .username(vo.getUsername())
                    .pw(passwordEncoder.encode(vo.getPw()))
                    .name(vo.getName())
                    .useyn("y")
                    .role(Role.ADMIN.getKey())
                    .build();

            memberRepository.save(admin);
            result = true;
        }
        return result;
    }

    @Override
    public boolean loginProcess(MemberVo vo) {
        boolean isMember = memberRepository.existsByUsername(vo.getUsername());

        if(isMember){
            Member member = memberRepository.findByUsername(vo.getUsername());
            boolean checkPw = member.getPw().equals(vo.getPw());

            if(checkPw){
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isUserName(MemberVo vo) {

        return memberRepository.existsByUsername(vo.getUsername());
    }

    @Override
    public boolean isNickName(MemberVo vo) {

        return memberRepository.existsByNickname(vo.getNickname());
    }

    @Override
    public boolean isEmail(String email) {
        return memberRepository.existsByEmail(email);
    }

    @Override
    public boolean validatePw(String pw) {
        String PATTERN_PW = "^(?=.*[a-zA-Z])((?=.*\\d)|(?=.*\\W)).{8,128}+$";
        boolean pwPattern = Pattern.matches(PATTERN_PW, pw);

        return pwPattern;
    }

    @Override
    public boolean validateUsername(String username) {
        String PATTERN_ID = "^[a-z]{1}[a-z0-9]{5,10}+$";
        boolean idPattern = Pattern.matches(PATTERN_ID, username);
        return idPattern;
    }

    @Override
    public String getUsername(String name, String email, String birth, String phone, String provider) {

        return memberRepository.getUsername(name, email, birth, phone, provider);
    }

    @Override
    public List<String> getEmailList() {
        return memberRepository.getEmailList();
    }

    @Override
    public boolean isMemberByPw(String name, String username, String email, String birth, String phone) {

        return memberRepository.existsMemberByPw(name, username, email, birth, phone);
    }

    @Override
    public Page<Recommend> getList(int page, Member member) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.asc("rseq"));
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));

        return this.recommendRepository.findAllByMember(member, pageable);
    }

    @Override
    public Page<Member> findAll(Pageable pageable) {
        return memberRepository.findAll(pageable);
    }

    @Override
    public Page<Member> findAllByUsername(String username, Pageable pageable) {
        return memberRepository.findAllByUsername(username, pageable);
    }

    @Override
    public Page<Member> findAllByName(String name, Pageable pageable) {
        return memberRepository.findAllByNickName(name, pageable);
    }

    @Override
    public Page<Member> findAllByProvider(String provider, Pageable pageable) {
        return memberRepository.findAllByProvider(provider, pageable);
    }

    @Override
    public Page<Member> findAllByEmail(String eamil, Pageable pageable) {
        return memberRepository.findAllByEmail(eamil, pageable);
    }

    @Override
    public Page<Member> findAllAdmin(Pageable pageable) {
        return memberRepository.findAllAdmin(pageable);
    }

    @Override
    public Page<Member> findAllAdminByUsername(String username, Pageable pageable) {
        return memberRepository.findAllAdminByUsername(username, pageable);
    }

    @Override
    public Page<Member> findAllAdminByName(String name, Pageable pageable) {
        return memberRepository.findAllAdminByName(name, pageable);
    }


}
