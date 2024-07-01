package com.demo.campingnavi.service;

import java.util.List;


import com.demo.campingnavi.domain.ReviewComment;

public interface ReviewCommentService {
	
	public void saveComment(ReviewComment vo);
	
	public List<ReviewComment> getCommentList(int vseq);
	
	public List<ReviewComment> getReplyCommentList(int parentCmseq);

	public void deletComment(int cmseq);
	
	public void deletAllComment(int vseq);

	public void updateCommentCount(int vseq);

	public List<ReviewComment> getCommentMemberList(int mseq);

	List<ReviewComment> getAuthorReviewCommentList(int mseq, int page, int pageSize);
}
