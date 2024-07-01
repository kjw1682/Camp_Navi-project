package com.demo.campingnavi.service;

import com.demo.campingnavi.domain.Reply;
import com.demo.campingnavi.repository.jpa.ReplyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReplyServiceImpl implements ReplyService{

    @Autowired
    private ReplyRepository replyRepository;

    @Override
    public void saveReply(Reply reply) {
        replyRepository.save(reply);
    }

    @Override
    public Reply findById(int reply_id) {
        return replyRepository.findById(reply_id);
    }

    @Override
    public Page<Reply> findAllByQna(int qseq, Pageable pageable) {
        return replyRepository.findAllByQna(qseq, pageable);
    }

    @Override
    public List<Reply> getReplyListByQseq(int qseq) {
        return replyRepository.getReplyListByQseq(qseq);
    }
}
