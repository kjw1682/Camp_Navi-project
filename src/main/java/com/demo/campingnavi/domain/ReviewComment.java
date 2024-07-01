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
@NoArgsConstructor
@AllArgsConstructor
@DynamicUpdate
@DynamicInsert
@Builder
@Entity
public class ReviewComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int cmseq; // 댓글 번호

    @ManyToOne
    @JoinColumn(name = "mseq", nullable = false)
    private Member member;

    @ManyToOne
    @JoinColumn(name = "vseq", nullable = false)
    private Review review;

    @Column(length = 500)
    private String content; // 내용

    @Temporal(value=TemporalType.TIMESTAMP)
    @ColumnDefault("sysdate")
    @Column(updatable=false)
    private Date createdAt; // 생성일

    @ManyToOne
    @JoinColumn(name = "parent_comment_cmseq")
    private ReviewComment parentComment; // 부모댓글
}