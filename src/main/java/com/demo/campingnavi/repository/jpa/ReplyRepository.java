package com.demo.campingnavi.repository.jpa;

import com.demo.campingnavi.domain.Reply;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReplyRepository extends JpaRepository<Reply, Integer> {

    @Query("SELECT r FROM Reply r WHERE r.qna.qseq = ?1 AND r.useyn = 'y'")
    Page<Reply> findAllByQna(int qseq, Pageable pageable);

    Reply findById(int reply_id);

    @Query("SELECT r FROM Reply r WHERE r.qna.qseq = ?1 AND r.useyn = 'y'")
    List<Reply> getReplyListByQseq(int qseq);

}
