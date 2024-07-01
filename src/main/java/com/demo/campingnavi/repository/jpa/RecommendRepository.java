package com.demo.campingnavi.repository.jpa;

import com.demo.campingnavi.domain.Member;
import com.demo.campingnavi.domain.Recommend;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

public interface RecommendRepository extends JpaRepository<Recommend, Integer> {

    boolean existsByMember_MseqAndCamp_Cseq(int mseq, int cseq);

    @Transactional
    @Modifying
    @Query("DELETE FROM Recommend e " +
            "WHERE e.member.mseq = ?1 " +
            "AND e.camp.cseq = ?2")
    void deleteByMemberAndCamp(int mseq, int cseq);

    @Query("SELECT r FROM Recommend r WHERE r.member.mseq = :mseq")
    List<Recommend> getAllListByMember(int mseq);

    Page<Recommend> findAllByMember(Member member, Pageable pageable);
}
