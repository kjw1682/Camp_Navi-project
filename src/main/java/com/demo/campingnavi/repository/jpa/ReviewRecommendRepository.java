package com.demo.campingnavi.repository.jpa;

import com.demo.campingnavi.domain.Member;
import com.demo.campingnavi.domain.Review;
import com.demo.campingnavi.domain.ReviewRecommend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface ReviewRecommendRepository extends JpaRepository<ReviewRecommend, Integer> {
    public Optional<ReviewRecommend> findByMemberAndReview(Member member, Review review);
    public List<ReviewRecommend> findByReview(Review review);
    public List<ReviewRecommend> findByMember(Member member);

    @Query("SELECT review From Review review, ReviewRecommend reviewRecommend, Member member WHERE review = reviewRecommend.review AND member = reviewRecommend.member ")
    public List<Review> getRcdReviewListByMember(Member member);

    boolean existsByMember_MseqAndReview_Vseq(int mseq, int vseq);

    @Transactional
    @Modifying
    @Query("DELETE FROM ReviewRecommend e " +
            "WHERE e.member.mseq = ?1 " +
            "AND e.review.vseq = ?2")
    void deleteByMemberAndReview(int mseq, int vseq);
}
