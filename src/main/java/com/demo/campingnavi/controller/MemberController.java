package com.demo.campingnavi.controller;

import com.demo.campingnavi.config.PathConfig;
import com.demo.campingnavi.domain.Member;
import com.demo.campingnavi.domain.Qna;
import com.demo.campingnavi.domain.Recommend;
import com.demo.campingnavi.domain.Review;
import com.demo.campingnavi.dto.CustomOauth2UserDetails;
import com.demo.campingnavi.dto.CustomSecurityUserDetails;
import com.demo.campingnavi.dto.MemberVo;
import com.demo.campingnavi.repository.jpa.MemberRepository;
import com.demo.campingnavi.service.MemberService;
import com.demo.campingnavi.service.QnaService;
import com.demo.campingnavi.service.RecommendService;
import com.demo.campingnavi.service.ReviewService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Controller
@RequestMapping("/member")
public class MemberController {

    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    RecommendService recommendService;
    @Autowired
    ReviewService reviewService;
    @Autowired
    QnaService qnaService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @GetMapping("/login")
    public String loginP(Model model) {
        return "member/loginPage";
    }

    @GetMapping("/join")
    public String joinP() {
        return "member/membership";
    }

    @PostMapping("/joinProc")
    public String joinProcess(MemberVo vo, Model model) {

        boolean result = memberService.joinProcess(vo);
        if(result) {
            return "redirect:/member/login";
        } else {
            return "redirect:/member/joinAlertView";
        }
    }

    @GetMapping("/joinAlertView")
    public String joinAlertView() {
        return "member/joinAlert";
    }

    @PostMapping("/validateUser")
    @ResponseBody
    public Map<String, Object> validateUser(@RequestParam(value = "username") String username) {
        Map<String, Object> result = new HashMap<>();
        boolean isUserName = memberRepository.existsByUsername(username);
        if (!isUserName) {
            result.put("result", "success");
        } else {
            result.put("result", "fail");
        }

        return result;
    }

    @PostMapping("/validateNickname")
    @ResponseBody
    public Map<String, Object> validateNickname(@RequestParam(value = "nickname") String nickname) {
        Map<String, Object> result = new HashMap<>();
        boolean isNickname = memberRepository.existsByNickname(nickname);
        if (!isNickname) {
            result.put("result", "success");
        } else {
            result.put("result", "fail");
        }

        return result;
    }

    @RequestMapping("/membershipAgree")
    public String membershipAgree() {
        return "member/membershipAgree";
    }

    @GetMapping("/mypage")
    public String mypageP(Model model,
                          @RequestParam(value = "page", defaultValue = "0") int page,
                          @RequestParam(value = "pageMaxDisplay", defaultValue = "10") int pageMaxDisplay) {
        // 인증 객체 생성
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = "";
        CustomSecurityUserDetails securityUserDetails;
        CustomOauth2UserDetails oauth2UserDetails;
        // 객체의 아이디를 얻기 위해서 타입 변환
        if (authentication.getPrincipal() instanceof CustomSecurityUserDetails) { // 사이트 회원
            securityUserDetails = (CustomSecurityUserDetails) authentication.getPrincipal();
            username = securityUserDetails.getUsername();
        } else { // SNS 로그인 회원
            oauth2UserDetails = (CustomOauth2UserDetails) authentication.getPrincipal();
            username = oauth2UserDetails.getUsername();
        }

        // 추출된 아이디로 회원 객체 생성
        Member member = memberRepository.findByUsername(username);
        // 찜목록 객체 생성
        Page<Recommend> paging = this.memberService.getList(page, member);
        // 뷰에 전송
        model.addAttribute("member", member);
        model.addAttribute("img", member.getImg());
        return "member/myPage";
    }

    @GetMapping("/mypage/oauth")
    public String oauthMypageP(Model model, @RequestParam(defaultValue = "0") int page) {
        // 인증 객체 생성
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = "";
        CustomSecurityUserDetails securityUserDetails;
        CustomOauth2UserDetails oauth2UserDetails;
        // 객체의 아이디를 얻기 위해서 타입 변환
        if (authentication.getPrincipal() instanceof CustomSecurityUserDetails) { // 사이트 회원
            securityUserDetails = (CustomSecurityUserDetails) authentication.getPrincipal();
            username = securityUserDetails.getUsername();
        } else { // SNS 로그인 회원
            oauth2UserDetails = (CustomOauth2UserDetails) authentication.getPrincipal();
            username = oauth2UserDetails.getUsername();
        }

        // 추출된 아이디로 회원 객체 생성
        Member member = memberRepository.findByUsername(username);
        // 찜목록 객체 생성
        Page<Recommend> paging = this.memberService.getList(page, member);
        // 뷰에 전송
        model.addAttribute("member", member);
        model.addAttribute("paging", paging);
        return "member/myPageOAuth";
    }

    @PostMapping("/mypage/edit/detail")
    @ResponseBody
    public Map<String, Object> myPageEdit(Model model, HttpSession session,
                                          @RequestParam("nickname") String nickname,
                                          @RequestParam("sex") String sex,
                                          @RequestParam(value = "birth", defaultValue = "") String birth,
                                          @RequestParam("phone") String phone,
                                          @RequestParam("phone2") String phone2,
                                          @RequestParam("addr1") String addr1,
                                          @RequestParam("addr2") String addr2,
                                          @RequestParam(value = "img", required = false) String img,
                                          @RequestParam(value = "file", required = false) MultipartFile file) {
        // 인증 객체 생성
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = "";
        CustomSecurityUserDetails securityUserDetails;
        CustomOauth2UserDetails oauth2UserDetails;
        // 객체의 아이디를 얻기 위해서 타입 변환
        if (authentication.getPrincipal() instanceof CustomSecurityUserDetails) { // 사이트 회원
            securityUserDetails = (CustomSecurityUserDetails) authentication.getPrincipal();
            username = securityUserDetails.getUsername();
        } else { // SNS 로그인 회원
            oauth2UserDetails = (CustomOauth2UserDetails) authentication.getPrincipal();
            username = oauth2UserDetails.getUsername();
        }

        // 추출된 아이디로 회원 객체 생성
        Map<String, Object> data = new HashMap<>();
        Member member = memberRepository.findByUsername(username);
        if (file != null && !file.isEmpty()) {
            String fileName = file.getOriginalFilename();
            String uuid = UUID.randomUUID().toString();
            String saveName = uuid + "_" + fileName;
            member.setImg2(saveName);
            try {
                String uploadPath = PathConfig.intelliJPath + saveName;
                boolean exists = PathConfig.isExistsPath();
                if(exists) {
                    file.transferTo(new File(PathConfig.realPath(uploadPath)));
                } else {
                    uploadPath = PathConfig.eclipsePath + saveName;
                    file.transferTo(new File(PathConfig.realPath(uploadPath)));
                }
            } catch (IllegalStateException | IOException e) {
                e.printStackTrace();
            }
        }
        member.setImg(img);
        member.setNickname(nickname);
        member.setSex(sex);
        if (!birth.isEmpty()) {
            member.setBirth(birth);
        }
        member.setPhone(phone + phone2);
        member.setAddr1(addr1);
        member.setAddr2(addr2);

        memberRepository.save(member);
        session.setAttribute("loginUser", member);

        data.put("nickname", nickname);
        data.put("sex", sex);
        data.put("birth", birth);
        data.put("phone", phone);
        data.put("phone2", phone2);
        data.put("addr1", addr1);
        data.put("addr2", addr2);
        data.put("img", img);
        return data;
    }

    @GetMapping("/mypage/delete/{mseq}")
    public String deleteMember(@PathVariable("mseq") int mseq,
                               Model model) {
        Member member = memberService.findById(mseq);
        if(member != null) {
            member.setUseyn("n");
            memberService.saveMember(member);
            model.addAttribute("msg", "삭제 성공");
            return "redirect:/";
        } else {
            model.addAttribute("msg", "일치하는 회원이 없습니다.");
            return "redirect:/member/mypage";
        }

    }

    @GetMapping("/mypage/paging")
    @ResponseBody
    public Page<Recommend> reloadList(HttpSession session, Pageable pageable) {

        Member member = (Member) session.getAttribute("loginUser");

        return recommendService.findAll(member, pageable);
    }

    @GetMapping("/mypage/review")
    @ResponseBody
    public Page<Review> reviewPaging(HttpSession session, Pageable pageable) {

        Member member = (Member) session.getAttribute("loginUser");

        return reviewService.findAllByMember(member, pageable);
    }

    @GetMapping("/mypage/qna")
    @ResponseBody
    public Page<Qna> qnaPaging(HttpSession session, Pageable pageable) {
        Member member = (Member) session.getAttribute("loginUser");
        return qnaService.findAllByMember(member, pageable);
    }

    @GetMapping("/search/{type}")
    public String searchView(@PathVariable String type) {
        if (type.equals("username")) {
            return "member/searchUsername";
        } else if (type.equals("password")){
            return "member/searchPassword";
        } else {
            return "member/loginPage";
        }

    }

    @PostMapping("/search/username")
    public String searchUsername(MemberVo vo, Model model) {
        String username = memberService.getUsername(vo.getName(), vo.getEmail(), vo.getBirth(), vo.getPhone(), vo.getProvider());
        model.addAttribute("username", username);
        return "member/searchUsername";
    }

    @PostMapping("/search/password")
    @ResponseBody
    public Map<String, Object> searchPw(@RequestParam("name") String name,
                           @RequestParam("username") String username,
                           @RequestParam("email") String email,
                           @RequestParam("birth") String birth,
                           @RequestParam("phone") String phone) {

        Map<String, Object> map = new HashMap<>();
        if (memberService.isMemberByPw(name, username, email, birth, phone)) {
            Member member = memberService.findByUsername(username);
            map.put("mseq", member.getMseq());
            map.put("result", "success");
        } else {
            map.put("result", "존재하는 회원 정보가 없습니다.");
        }

        return map;
    }

    @PostMapping("/search/password/new")
    @ResponseBody
    public Map<String, Object> searchPw(@RequestParam("pw") String pw,
                                        @RequestParam("mseq") int mseq) {

        Map<String, Object> map = new HashMap<>();
        if (mseq != 0 && pw != null) {
            if(memberService.validatePw(pw)) {
                Member member = memberService.findById(mseq);
                member.setPw(passwordEncoder.encode(pw));
                memberService.saveMember(member);
                map.put("result", "success");
            } else {
                map.put("result", "비밀번호는 8자 이상이어야 하며, 영문 대/소문자와 특수문자가 1개 이상 포함되어야 합니다.");
            }

        } else {
            map.put("result", "공백은 비밀번호로 사용할 수 없습니다.");
        }

        return map;
    }
}
