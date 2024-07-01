package com.demo.campingnavi.dto;

import com.demo.campingnavi.domain.Member;
import com.demo.campingnavi.domain.Qna;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@ToString
public class ReplyVo {
    private int reply_id;
    private String content;
    private MultipartFile img;
}
