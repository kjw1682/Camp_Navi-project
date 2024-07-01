package com.demo.campingnavi.repository.jpa;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.demo.campingnavi.domain.ReviewComment;

import java.util.List;

public interface ReviewCommentRepository extends JpaRepository<ReviewComment, Integer> {

	@Query("SELECT c FROM ReviewComment c " +
			"WHERE c.review.vseq = ?1 AND c.parentComment IS NULL " +
			"ORDER BY c.cmseq DESC")
	List<ReviewComment> findParentReviewCommentByVseq(int vseq);

	@Query("SELECT c FROM ReviewComment c " +
			"WHERE c.parentComment.cmseq = ?1 " +
			"ORDER BY c.cmseq ASC")
	List<ReviewComment> findRepliesByParentCommentCmseq(int parentCmseq);

	// 게시글 번호(vseq)에 해당하는 댓글들을 모두 삭제
	@Transactional
	void deleteByReviewVseq(int vseq);

	@Query("SELECT COUNT(c) FROM ReviewComment c WHERE c.review.vseq = :vseq")
	int countByReviewVseq(int vseq);

	@Query("SELECT c FROM ReviewComment c " +
			"WHERE c.member.mseq = ?1 " +  // 공백 추가
			"ORDER BY c.cmseq DESC")
	List<ReviewComment> findReviewCommentByMseq(int mseq);

	@Transactional
	void deleteByParentComment_Cmseq(int cmseq);

	@Query("SELECT r FROM ReviewComment r WHERE r.member.mseq = ?1 ORDER BY r.createdAt DESC")
	Page<ReviewComment> findAuthorList(int mseq, PageRequest pageRequest);
}