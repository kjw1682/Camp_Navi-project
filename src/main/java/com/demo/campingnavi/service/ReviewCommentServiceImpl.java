package com.demo.campingnavi.service;

import java.util.List;

import com.demo.campingnavi.domain.Review;
import com.demo.campingnavi.repository.jpa.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.demo.campingnavi.domain.ReviewComment;
import com.demo.campingnavi.repository.jpa.ReviewCommentRepository;

@Service
public class ReviewCommentServiceImpl implements ReviewCommentService {

	@Autowired
	ReviewRepository reviewRepo;
	@Autowired
	ReviewCommentRepository reviewCommentRepo;

	@Override
	public void saveComment(ReviewComment vo) {
		reviewCommentRepo.save(vo);
		updateCommentCount(vo.getReview().getVseq());
	}

	@Override
	public List<ReviewComment> getCommentList(int vseq) {
		return reviewCommentRepo.findParentReviewCommentByVseq(vseq);
	}



	@Override
	public List<ReviewComment> getReplyCommentList(int parentCmseq) {
		return reviewCommentRepo.findRepliesByParentCommentCmseq(parentCmseq);
	}


	@Override
	public void deletComment(int cmseq) {
		ReviewComment comment = reviewCommentRepo.findById(cmseq).orElse(null);
		if (comment != null) {
			reviewCommentRepo.deleteByParentComment_Cmseq(cmseq);
			reviewCommentRepo.deleteById(cmseq);
			updateCommentCount(comment.getReview().getVseq());
		}
	}

	@Override
	public void deletAllComment(int vseq) {
		reviewCommentRepo.deleteByReviewVseq(vseq);
		updateCommentCount(vseq);
	}

	@Override
	public void updateCommentCount(int vseq) {
		Review review = reviewRepo.findById(vseq).orElse(null);
		if (review != null) {
			review.setCommentCount(reviewCommentRepo.countByReviewVseq(vseq));
			reviewRepo.save(review);
		}

	}

	@Override
	public List<ReviewComment> getCommentMemberList(int mseq) {
		return reviewCommentRepo.findReviewCommentByMseq(mseq);
	}

	@Override
	public List<ReviewComment> getAuthorReviewCommentList(int mseq, int page, int pageSize) {
		PageRequest pageRequest = PageRequest.of(page - 1, pageSize);
		Page<ReviewComment> reviewPage = reviewCommentRepo.findAuthorList(mseq, pageRequest);
		return reviewPage.getContent();
	}
}
