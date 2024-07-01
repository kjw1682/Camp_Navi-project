package com.demo.campingnavi.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.util.Date;

@Getter
@Setter
@ToString
@DynamicInsert
@DynamicUpdate
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int vseq; // 후기 번호

    @ManyToOne
    @JoinColumn(name="mseq", nullable=false)
    private Member member;

    @ManyToOne
    @JoinColumn(name="cseq", nullable=false)
    private Camp camp;

    @Column(length = 500, nullable = false)
    private String title; // 제목

    @Column(length = 1000, nullable = false)
    private String content; // 내용

    @Column(length = 1000)
    private String image; // 이미지

    @Temporal(value=TemporalType.TIMESTAMP)
    @ColumnDefault("sysdate")
    @Column(updatable=false)
    private Date createdAt; // 생성일

    @Temporal(value=TemporalType.TIMESTAMP)
    @ColumnDefault("sysdate")
    private Date modifiedAt; // 수정일

    @Column(length = 10, nullable = false)
    private float likes; // 평점(만점 5점)

    @ColumnDefault("0")
    private int count; // 조회수

    @ColumnDefault("0")
    private int commentCount; // 댓글수
}