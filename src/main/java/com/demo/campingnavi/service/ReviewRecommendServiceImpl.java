package com.demo.campingnavi.service;

import com.demo.campingnavi.domain.Member;
import com.demo.campingnavi.domain.Review;
import com.demo.campingnavi.domain.ReviewRecommend;
import com.demo.campingnavi.repository.jpa.ReviewRecommendRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReviewRecommendServiceImpl implements ReviewRecommendService {

    @Autowired
    ReviewRecommendRepository reviewRecommendRepo;


    @Override
    public int rcdStatus(Member member, Review review) {
        int result = 0;
        Optional<ReviewRecommend> rcdOp = reviewRecommendRepo.findByMemberAndReview(member, review);
        if (rcdOp.isPresent()) {
            result++;
        }
        return result;
    }

    @Override
    public int rcdUpdate(Member member, Review review) {
        int result = 0;
        Optional<ReviewRecommend> rcdOp = reviewRecommendRepo.findByMemberAndReview(member, review);
        if (rcdOp.isEmpty()) {
            ReviewRecommend rcd = new ReviewRecommend();
            rcd.setMember(member);
            rcd.setReview(review);
            reviewRecommendRepo.save(rcd);
            result = 1;
        } else {
            reviewRecommendRepo.delete(rcdOp.get());
        }
        return result;
    }

    @Override
    public int getRcdCountByReview(Review review) {
        List<ReviewRecommend> rcdList = reviewRecommendRepo.findByReview(review);
        return rcdList.size();
    }

    @Override
    public List<Review> getRcdReviewListByMember(Member member) {
        return reviewRecommendRepo.getRcdReviewListByMember(member);
    }

    @Override
    public void addReviewRecommend(ReviewRecommend reviewRecommend) {
        reviewRecommendRepo.save(reviewRecommend);
    }

    @Override
    public void removeReviewRecommend(int mseq, int vseq) {
        reviewRecommendRepo.deleteByMemberAndReview(mseq, vseq);
    }

    @Override
    public boolean checkReviewRecommend(int mseq, int vseq) {
        return reviewRecommendRepo.existsByMember_MseqAndReview_Vseq(mseq, vseq);
    }


}
