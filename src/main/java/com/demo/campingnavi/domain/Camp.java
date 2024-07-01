package com.demo.campingnavi.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@DynamicInsert
@DynamicUpdate
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Camp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int cseq; // 캠핑장 번호

    @Column(length = 20, nullable = false)
    private String contentId; // 캠핑장 컨텐츠 아이디(이미지 조회 시 필요)

    @Column(length = 500, nullable = false)
    private String name; // 캠핑장 이름

    @Column(length = 100, nullable = true)
    private LocalDate createdAt; // 캠핑장 설립일

    @Column(length = 1000, nullable = false)
    private String campType; // 캠핑장 종류(일반, 카라반, 자동차 등등)

    @Column(length = 1000, nullable = false)
    private String addr1; // 캠핑장 주소

    @Column(length = 1000)
    private String addr2; // 캠핑장 상세 주소

    @Column(length = 100, nullable = false)
    private String mapX; // 경도

    @Column(length = 100, nullable = false)
    private String mapY; // 위도

    @Column(length = 1, nullable = false)
    private String useyn; // 사용여부

    @Column(length = 1000)
    private String image; // 이미지

    @Column(length = 200)
    private String reservationLink; // 예약링크

    @Column(length = 50)
    private String locationB; // 소재지(시도)

    @Column(length = 50)
    private String locationS; // 소재지(시군구)
}
