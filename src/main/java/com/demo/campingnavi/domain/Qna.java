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
public class Qna {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int qseq; // 문의 번호

    @ManyToOne
    @JoinColumn(name="mseq", nullable=false)
    private Member member;

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

    @Column(length = 1, nullable = false)
    private String checkyn; // 답변 여부

    @Column(length = 1)
    private String useyn;

    @Column
    private String type;
}