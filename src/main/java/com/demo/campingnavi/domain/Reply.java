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
public class Reply {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int reply_id;

    @Column(nullable = false, length = 2000)
    private String content;

    @ManyToOne
    @JoinColumn(name = "qseq", nullable = false)
    private Qna qna;

    @Temporal(value=TemporalType.TIMESTAMP)
    @ColumnDefault("sysdate")
    @Column(updatable=false)
    private Date createdAt; // 생성일

    @ManyToOne
    @JoinColumn(name = "mseq", nullable = false)
    private Member member;

    @Column
    private String img;

    @Column
    private String useyn;
}
