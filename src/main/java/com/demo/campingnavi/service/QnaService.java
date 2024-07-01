package com.demo.campingnavi.service;

import com.demo.campingnavi.domain.Member;
import com.demo.campingnavi.domain.Qna;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface QnaService {

    public void saveQna(Qna qna);

    public Qna findById(int qseq);

    public Page<Qna> findAllByType(String type, Pageable pageable);

    public Page<Qna> findAllByTypeExceptNone(String type, Pageable pageable);

    public Page<Qna> findAllByUsername(String type, String username, Pageable pageable);

    public Page<Qna> findAllByNickName(String type, String nickname, Pageable pageable);

    public Page<Qna> findAllByTitle(String type, String title, Pageable pageable);

    public Page<Qna> findAllByContent(String type, String content, Pageable pageable);

    public Page<Qna> findAllByMember(Member member, Pageable pageable);
}
