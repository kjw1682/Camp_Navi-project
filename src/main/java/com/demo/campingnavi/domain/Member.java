package com.demo.campingnavi.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Getter
@Setter
@ToString
@DynamicInsert
@DynamicUpdate
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Member {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int mseq; // 회원 번호
    
    @Column(length = 50, nullable = false, unique = true)
    private String username; // 회원 아이디

    @Column(length = 100)
    private String pw; // 회원 비밀번호

    @Column(length = 200)
    private String email; // 회원 이메일

    @Column(length = 1000)
    private String addr1; // 회원 주소

    @Column(length = 1000)
    private String addr2; // 회원 상세 주소

    @Column(length = 50)
    private String phone; // 회원 전화번호

    @Column(length = 1)
    private String sex; // 회원 성별

    @Column(length = 50, unique = true)
    private String nickname; // 회원 닉네임

    @Column(length = 50, nullable = false)
    private String name; // 회원 이름

    @Column(length = 1, nullable = false)
    private String useyn; // 사용여부(탈퇴: n, 계속 사용: y)

    @Column(length = 50)
    private String birth; // 회원 생년월일(1999-11-11) 형식

    @Column
    private String role; // 일반회원: USER, 관리자: ADMIN

    @Lob
    @Column
    private String img;

    @Column
    private String img2;

    private String provider;

    private String providerId;
}
