package com.demo.campingnavi.service;

import com.demo.campingnavi.domain.Member;
import com.demo.campingnavi.domain.Qna;
import com.demo.campingnavi.repository.jpa.QnaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class QnaServiceImpl implements QnaService{

    @Autowired
    QnaRepository qnaRepository;

    @Override
    public void saveQna(Qna qna) {
        qnaRepository.save(qna);
    }

    @Override
    public Qna findById(int qseq) {
        return qnaRepository.findById(qseq);
    }

    @Override
    public Page<Qna> findAllByType(String type, Pageable pageable) {
        return qnaRepository.findAllByType(type, pageable);
    }

    @Override
    public Page<Qna> findAllByTypeExceptNone(String type, Pageable pageable) {
        return qnaRepository.findAllByTypeExceptNone(type, pageable);
    }

    @Override
    public Page<Qna> findAllByUsername(String type, String username, Pageable pageable) {
        return qnaRepository.findAllByUsername(type, username, pageable);
    }

    @Override
    public Page<Qna> findAllByNickName(String type, String nickname, Pageable pageable) {
        return qnaRepository.findAllByNickName(type, nickname, pageable);
    }

    @Override
    public Page<Qna> findAllByTitle(String type, String title, Pageable pageable) {
        return qnaRepository.findAllByTitle(type, title, pageable);
    }

    @Override
    public Page<Qna> findAllByContent(String type, String content, Pageable pageable) {
        return qnaRepository.findAllByContent(type, content, pageable);
    }

    @Override
    public Page<Qna> findAllByMember(Member member, Pageable pageable) {
        return qnaRepository.findAllByMember(member, pageable);
    }
}
