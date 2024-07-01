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
public class Admin {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int aseq; // 관리자 번호
    
    @Column(length = 50, nullable = false)
    private String id; // 관리자 아이디

    @Column(length = 100, nullable = false)
    private String pw; // 관리자 비밀번호

    @Column(length = 50, nullable = false)
    private String name; // 관리자 이름

    @Column(length = 1, nullable = false)
    private String useyn; // 사용여부(탈퇴: n, 계속 사용: y)
}
