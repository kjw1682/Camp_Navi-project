package com.demo.campingnavi.service;

import com.demo.campingnavi.domain.Member;
import com.demo.campingnavi.domain.Review;
import com.demo.campingnavi.domain.ReviewRecommend;

import java.util.List;

public interface ReviewRecommendService {
    public int rcdStatus(Member member, Review review);
    public int rcdUpdate(Member member, Review review);
    public int getRcdCountByReview(Review review);
    public List<Review> getRcdReviewListByMember(Member member);

    void addReviewRecommend(ReviewRecommend reviewRecommend);

    void removeReviewRecommend(int mseq, int vseq);

    boolean checkReviewRecommend(int mseq, int vseq);
}
