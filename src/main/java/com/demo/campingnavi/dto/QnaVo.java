package com.demo.campingnavi.dto;

import com.demo.campingnavi.domain.Member;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class QnaVo {

    private int qseq;
    private String title;
    private String content;
    private Member member;
    private MultipartFile img;

}
