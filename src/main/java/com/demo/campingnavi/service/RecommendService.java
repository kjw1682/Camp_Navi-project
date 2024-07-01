package com.demo.campingnavi.service;

import com.demo.campingnavi.domain.Member;
import com.demo.campingnavi.domain.Recommend;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public interface RecommendService {
    public Page<Recommend> findAll(Member member, Pageable pageable);
}
