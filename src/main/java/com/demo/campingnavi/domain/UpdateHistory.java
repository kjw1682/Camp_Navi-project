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
public class UpdateHistory {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int useq; // 업데이트 번호

    @Column(length = 100, nullable = false)
    private String kind;

    @Temporal(value=TemporalType.TIMESTAMP)
    @ColumnDefault("sysdate")
    private Date updateTime;

    @Column(length = 10, nullable = false)
    private String result;
}
