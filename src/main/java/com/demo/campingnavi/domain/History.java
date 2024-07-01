package com.demo.campingnavi.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDate;
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
public class History {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int hseq; // 기록 번호

    @ManyToOne
    @JoinColumn(name="mseq", nullable=false)
    private Member member;

    @ManyToOne
    @JoinColumn(name="cseq", nullable=false)
    private Camp camp;

    private LocalDate reservationDate; // 캠핑장 다녀온 날짜
}