package com.demo.campingnavi.service;

import com.demo.campingnavi.domain.Member;
import com.demo.campingnavi.domain.Recommend;
import com.demo.campingnavi.repository.jpa.RecommendRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RecommendServiceImpl implements RecommendService{

    private final RecommendRepository recommendRepository;

    @Override
    public Page<Recommend> findAll(Member member, Pageable pageable) {

        return recommendRepository.findAllByMember(member, pageable);
    }
}
