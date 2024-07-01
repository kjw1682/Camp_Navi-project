package com.demo.campingnavi.repository.jpa;

import com.demo.campingnavi.domain.Member;
import com.demo.campingnavi.domain.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Integer> {
    List<Review> findAllByOrderByCreatedAtDesc();

    @Modifying
    @Query("update Review r set r.count = r.count+1 where r.vseq = :vseq")
    int updateCount(@Param("vseq") int vseq);

    @Query("SELECT r FROM Review r "
            + "WHERE r.title LIKE %:title% "
            + "or r.content LIKE %:content% OR r.content IS NULL ")
    List<Review> findReviewList(String title, String content);

    List<Review> findByTitleContaining(String title);

    List<Review> findByMemberUsername(String username);

    List<Review> findByContentContaining(String writer);

    List<Review> findByCampNameContaining(String campName);

    @Query("SELECT r FROM Review r ORDER BY r.count DESC")
    List<Review> findBestList();

    @Query("SELECT r FROM Review r WHERE r.member.mseq = ?1 ORDER BY r.createdAt DESC")
    List<Review> findAuthorList(int mseq);

    @Query("SELECT r From Review r WHERE r.camp.cseq = ?1 ORDER BY r.createdAt DESC")
    List<Review> findCampReviewList(int cseq);

    Review findFirstByOrderByVseqDesc();

    // 특정 cseq를 가진 리뷰를 페이지네이션하여 조회
    @Query("SELECT r FROM Review r WHERE r.camp.cseq = ?1 ORDER BY r.createdAt DESC")
    Page<Review> findByCampCseq(int cseq, Pageable pageable);

    Page<Review> findAllByMember(Member member, Pageable pageable);

    // 특정 cseq를 가진 리뷰의 총 개수

    @Query("SELECT COUNT(r) FROM Review r WHERE r.camp.cseq = :cseq")
    long countByCampCseq(@Param("cseq") int cseq);

    @Query("SELECT r FROM Review r WHERE r.member.mseq = ?1 ORDER BY r.createdAt DESC")
    Page<Review> findAuthorList(int mseq, PageRequest pageRequest);

}