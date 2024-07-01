package com.demo.campingnavi;

import com.demo.campingnavi.config.PathConfig;
import com.demo.campingnavi.domain.Qna;
import com.demo.campingnavi.domain.Role;
import com.demo.campingnavi.repository.jpa.CampRepository;
import com.demo.campingnavi.repository.jpa.QnaRepository;
import com.demo.campingnavi.service.DataService;
import com.demo.campingnavi.domain.Camp;
import com.demo.campingnavi.domain.Member;
import com.demo.campingnavi.repository.jpa.MemberRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class DataTest {
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private DataService dataService;
    @Autowired
    private QnaRepository qnaRepository;
    @Autowired
    private CampRepository campRepository;

    @Disabled
    @Test
    void memberInputTest() {
        Member m = Member.builder()
                .username("123")
                .pw("123")
                .addr1("123")
                .addr2("123")
                .email("123")
                .name("123")
                .phone("123")
                .sex("m")
                .birth("123")
                .useyn("y")
                .nickname("123")
                .build();
        memberRepository.save(m);
    }

    @Disabled
    @Test
    public void CampInput () {
        String csvFile = "campingData.csv";
        String n = "all";
        List<Camp> campList = dataService.campInFromCsv(csvFile, n);

    }

    @Disabled
    @Test
    void CampDelete() {
        List<Camp> campList = campRepository.findAll();
        for (Camp camp : campList) {
            campRepository.delete(camp);
        }
    }

    @Disabled
    @Test
    void faqInsertTest() {
        Qna qna = Qna.builder()
                .member(memberRepository.findByUsername("test33"))
                .title("FAQ TEST1")
                .content("FAQ 테스트 1번 입니다.")
                .checkyn("n")
                .useyn("y")
                .type("FAQ")
                .build();

        qnaRepository.save(qna);
    }
}
