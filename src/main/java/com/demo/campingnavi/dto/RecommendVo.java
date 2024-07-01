package com.demo.campingnavi.dto;

import com.demo.campingnavi.domain.Recommend;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Collections;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RecommendVo {
    private Page<Recommend> recommendList;
    private int totalPages = 0;
    private int page = 0;
    private int size = 10;
    private int pageMaxDisplay = 10;
    private String sortDirection = "DESC";
}
