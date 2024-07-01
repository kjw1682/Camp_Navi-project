package com.demo.campingnavi.service;

import java.util.ArrayList;
import java.util.List;

import com.demo.campingnavi.domain.Member;
import com.demo.campingnavi.dto.ReviewVo;
import com.demo.campingnavi.repository.jpa.ReviewRecommendRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.demo.campingnavi.domain.Review;
import com.demo.campingnavi.dto.ReviewScanVo;
import com.demo.campingnavi.repository.jpa.ReviewRepository;

import jakarta.transaction.Transactional;

@Service
public class ReviewServiceImpl implements ReviewService {
	
	@Autowired
	private ReviewRepository reviewRepo;
    @Autowired
    private ReviewRecommendService reviewRecommendService;

	@Override
	public void insertReview(Review vo) {
		reviewRepo.save(vo);
	}

	@Override
	public Review getReview(int vseq) {
		return reviewRepo.findById(vseq).get();
	}

	@Override
	public void editReview(Review vo) {
		reviewRepo.save(vo);
	}

	@Override
	public void deleteReview(int vseq) {
	    reviewRepo.deleteById(vseq); 
	}
	
	@Override
	@Transactional
	public int updateCnt(int vseq) {
		return reviewRepo.updateCount(vseq);
	}

	@Override
	public List<ReviewVo> getBestReviewVoList() {
		List<Review> reviewList = reviewRepo.findBestList();
		List<ReviewVo> reviewVoList = new ArrayList<>();
		for (Review vo : reviewList) {
			int rcdCount = reviewRecommendService.getRcdCountByReview(vo);
			ReviewVo reviewVo = new ReviewVo(vo, rcdCount);
			reviewVoList.add(reviewVo);
		}
		return reviewVoList;
	}

	@Override
	public List<ReviewVo> getAuthorReviewVoList(int mseq) {
		List<Review> reviewList = reviewRepo.findAuthorList(mseq);
		List<ReviewVo> reviewVoList = new ArrayList<>();
		for (Review vo : reviewList) {
			int rcdCount = reviewRecommendService.getRcdCountByReview(vo);
			ReviewVo reviewVo = new ReviewVo(vo, rcdCount);
			reviewVoList.add(reviewVo);
		}
		return reviewVoList;
	}

	@Override
	public List<ReviewVo> getReviewVoListByCseq(int cseq) {
		List<Review> reviewList = reviewRepo.findCampReviewList(cseq);
		List<ReviewVo> reviewVoList = new ArrayList<>();
		for (Review vo : reviewList) {
			int rcdCount = reviewRecommendService.getRcdCountByReview(vo);
			ReviewVo reviewVo = new ReviewVo(vo, rcdCount);
			reviewVoList.add(reviewVo);
		}
		return reviewVoList;
	}

	@Override
	public Review getLastReview() {
		return reviewRepo.findFirstByOrderByVseqDesc();
	}

	@Override
	public List<Review> getReviewListByCseq(int cseq, int page, int pageSize) {
		PageRequest pageRequest = PageRequest.of(page - 1, pageSize);
		Page<Review> reviewPage = reviewRepo.findByCampCseq(cseq, pageRequest);
		return reviewPage.getContent();
	}

	@Override
	public long getTotalReviewsByCampId(int cseq) {
		return reviewRepo.countByCampCseq(cseq);
	}

	@Override
	public Page<Review> findAllByMember(Member member, Pageable pageable) {

		return reviewRepo.findAllByMember(member, pageable);
	}

	@Override
	public List<Review> getAuthorReviewVoList(int mseq, int page, int pageSize) {
		PageRequest pageRequest = PageRequest.of(page - 1, pageSize);
		Page<Review> reviewPage = reviewRepo.findAuthorList(mseq, pageRequest);
		return reviewPage.getContent();
	}


	@Override
	public List<ReviewVo> findReviewVoList(ReviewScanVo reviewScanVo) {
	    String searchField = reviewScanVo.getSearchField();
	    String searchWord = reviewScanVo.getSearchWord();

	    List<Review> reviewList = null;
	    switch (searchField) {
	        case "title":
				reviewList = reviewRepo.findByTitleContaining(searchWord);
	            break;
	        case "content":
				reviewList = reviewRepo.findByContentContaining(searchWord);
	            break;
	        case "writer":
				reviewList = reviewRepo.findByMemberUsername(searchWord);
	            break;
	        case "titleContent":
				reviewList = reviewRepo.findReviewList(searchWord, searchWord);
	            break;
			case "campName" :
				reviewList = reviewRepo.findByCampNameContaining(searchWord);
				break;
	        default:
				reviewList = reviewRepo.findAllByOrderByCreatedAtDesc();
	            break;
	    }

		List<ReviewVo> reviewVoList = new ArrayList<>();
		for (Review vo : reviewList) {
			int rcdCount = reviewRecommendService.getRcdCountByReview(vo);
			ReviewVo reviewVo = new ReviewVo(vo, rcdCount);
			reviewVoList.add(reviewVo);
		}

	    return reviewVoList;
	}

}
