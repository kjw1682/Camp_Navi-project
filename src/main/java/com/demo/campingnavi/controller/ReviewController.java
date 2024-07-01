package com.demo.campingnavi.controller;

import com.demo.campingnavi.domain.*;
import com.demo.campingnavi.dto.*;
import com.demo.campingnavi.service.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/review")
public class ReviewController {

    @Autowired
    ReviewService reviewService;

    @Autowired
    ReviewCommentService reviewCommentService;

    @Autowired
    ReviewRecommendService reviewRecommendService;

    @Autowired
    CampService campService;

    @Autowired
    DataService dataService;


    //게시글 작성으로 이동
    @GetMapping("/insert_form")
    public String showWriteForm(HttpSession session, Model model,
                                @RequestParam(value="cseq", defaultValue="1") int cseq) {
        // 세션에서 사용자 정보 가져오기
        Member member = (Member) session.getAttribute("loginUser");
       // MemberVo memberVo = new MemberVo();
        // 세션에 로그인 정보가 없는 경우
        if (member == null) {
            // 로그인 알림을 포함한 경고 메시지를 설정합니다.
            model.addAttribute("msg","로그인 후 이용해주세요.");
            model.addAttribute("redirectTo","/");
            return "review/review_alert";
        } else {
            Camp camp = campService.getCampByCseq(cseq);
            model.addAttribute("camp", camp);
            return "review/reviewInsert"; //게시글 작성페이지로 이동.
        }

    }

    @GetMapping("/insert_search_form")
    public String showWriteSearchForm(HttpSession session, Model model
                                ) {
        // 세션에서 사용자 정보 가져오기
        Member member = (Member) session.getAttribute("loginUser");
        // MemberVo memberVo = new MemberVo();
        // 세션에 로그인 정보가 없는 경우
        if (member == null) {
            // 로그인 알림을 포함한 경고 메시지를 설정합니다.
            model.addAttribute("msg","로그인 후 이용해주세요.");
            model.addAttribute("redirectTo","/");
            return "review/review_alert";
        } else {

            return "review/reviewSearchInsert"; //게시글 작성페이지로 이동.
        }

    }


    // 게시글 작성
    @PostMapping("/insert")
    public String saveReview(@RequestParam(value = "title") String title,
                             @RequestParam(value = "content") String content,
                             @RequestParam(value = "cseq", defaultValue = "0") int cseq,
                             @RequestParam(value = "likes", defaultValue = "5.0") float likes,
                             HttpSession session,
                             HttpServletRequest request,
                             Model model) {

        // 세션에서 사용자 정보 가져오기
        Member member = (Member) session.getAttribute("loginUser");
        MemberVo memberVo = new MemberVo();
        // 세션에 로그인 정보가 없는 경우
        if (member == null) {
            // 로그인 알림을 포함한 경고 메시지를 설정합니다.
            model.addAttribute("msg","로그인 후 이용해주세요.");
            model.addAttribute("redirectTo","/");
            return "review/review_alert";
        }

        Review vo = new Review();

        if (title.isEmpty()) {
            vo.setTitle("제목 없음");
        }else {
            vo.setTitle(title);
        }
        vo.setContent(content);
        vo.setMember(member); // 사용자 정보 설정

        vo.setCamp(campService.getCampByCseq(cseq));
        vo.setLikes(likes);
        vo.setCount(0);

        model.addAttribute("memberVo", memberVo);
        reviewService.insertReview(vo);
        vo = reviewService.getLastReview();
        List<Review> reviewList = new ArrayList<>();
        reviewList.add(vo);
        dataService.reviewListOutToCsv(reviewList);

        return "redirect:/review/list"; // 저장 후 리스트 페이지로 리다이렉트합니다.
    }

    // 게시글 작성
    @PostMapping("/search/insert")
    public String saveReview(@RequestParam(value = "title") String title,
                             @RequestParam(value = "content") String content,
                             @RequestParam(value = "camp", defaultValue = "0") String name,
                             @RequestParam(value = "likes", defaultValue = "5.0") float likes,
                             HttpSession session,
                             HttpServletRequest request,
                             Model model) {

        // 세션에서 사용자 정보 가져오기
        Member member = (Member) session.getAttribute("loginUser");
        MemberVo memberVo = new MemberVo();
        // 세션에 로그인 정보가 없는 경우
        if (member == null) {
            // 로그인 알림을 포함한 경고 메시지를 설정합니다.
            model.addAttribute("msg","로그인 후 이용해주세요.");
            model.addAttribute("redirectTo","/");
            return "review/review_alert";
        }
        Review vo = new Review();

        if (title.isEmpty()) {
            vo.setTitle("제목 없음");
        }else {
            vo.setTitle(title);
        }

        vo.setContent(content);
        vo.setMember(member); // 사용자 정보 설정

        vo.setCamp(campService.getCampByName(name));
        vo.setLikes(likes);
        vo.setCount(0);

        model.addAttribute("memberVo", memberVo);
        reviewService.insertReview(vo);
        vo = reviewService.getLastReview();
        List<Review> reviewList = new ArrayList<>();
        reviewList.add(vo);
        dataService.reviewListOutToCsv(reviewList);

        return "redirect:/review/list"; // 저장 후 리스트 페이지로 리다이렉트합니다.
    }


    // 게시글 리스트 보기
    @GetMapping("/list")
    public String showReviewList(Model model,
                                @RequestParam(value = "page", defaultValue = "0") int page,
                                @RequestParam(value = "size", defaultValue = "5") int size,
                                @RequestParam(value = "sortBy", defaultValue = "vseq") String sortBy,
                                @RequestParam(value = "sortDirection", defaultValue = "DESC") String sortDirection,
                                @RequestParam(value = "pageMaxDisplay", defaultValue = "10") int pageMaxDisplay,
                                @RequestParam(value = "searchField", defaultValue = "") String searchField,
                                @RequestParam(value = "searchWord", defaultValue = "") String searchWord,
                                ReviewScanVo reviewScanVo,
                                HttpSession session) {

        // 세션에서 사용자 정보 가져오기
        Member member = (Member) session.getAttribute("loginUser");
        // MemberVo memberVo = new MemberVo(member);
        // 세션에 로그인 정보가 없는 경우
        if (member == null) {
            // 로그인 알림을 포함한 경고 메시지를 설정합니다.
            model.addAttribute("msg","로그인 후 이용해주세요.");
            model.addAttribute("redirectTo","/");
            return "review/review_alert";
        }

        if (page == 0) {
            reviewScanVo = new ReviewScanVo(); // 새로운 객체로 초기화
            reviewScanVo.setSearchField(searchField);
            reviewScanVo.setSearchWord(searchWord);
            reviewScanVo.setPage(1);
            reviewScanVo.setSize(size);
            reviewScanVo.setSortBy(sortBy);
            reviewScanVo.setSortDirection(sortDirection);
            reviewScanVo.setPageMaxDisplay(pageMaxDisplay);
            List<ReviewVo> reviewVoList = reviewService.findReviewVoList(reviewScanVo);
            reviewScanVo.setReviewVoList(reviewVoList);
            reviewScanVo.setReviewVoBestList(reviewService.getBestReviewVoList());
            reviewScanVo.setTotalPages((reviewScanVo.getReviewVoList().size()+ reviewScanVo.getSize()-1)/ reviewScanVo.getSize());
            session.setAttribute("reviewScanVo", reviewScanVo);
        } else {
            reviewScanVo = (ReviewScanVo) session.getAttribute("reviewScanVo");
            reviewScanVo.setPage(page);
            List<ReviewVo> reviewVoList = reviewService.findReviewVoList(reviewScanVo);
            reviewScanVo.setReviewVoList(reviewVoList);
            reviewScanVo.setReviewVoBestList(reviewService.getBestReviewVoList());
            reviewScanVo.setTotalPages((reviewScanVo.getReviewVoList().size()+ reviewScanVo.getSize()-1)/ reviewScanVo.getSize());
            session.setAttribute("reviewScanVo", reviewScanVo);
        }

        reviewScanVo = (ReviewScanVo) session.getAttribute("reviewScanVo");
        model.addAttribute("reviewScanVo", reviewScanVo);

        return "review/reviewList";
    }

    // 게시글 상세보기
    @GetMapping("/detail/{vseq}")
    public String reviewDetail(@PathVariable("vseq") int vseq, Model model, HttpSession session) {

        // 세션에서 사용자 정보 가져오기
        Member member = (Member) session.getAttribute("loginUser");
        // MemberVo memberVo = new MemberVo(member);
        // 세션에 로그인 정보가 없는 경우
        if (member == null) {
            // 로그인 알림을 포함한 경고 메시지를 설정합니다.
            model.addAttribute("msg","로그인 후 이용해주세요.");
            model.addAttribute("redirectTo","/");
            return "review/review_alert";
        }

        // 게시글 번호를 통해 해당 게시글 가져오기
        Review review = reviewService.getReview(vseq);
        ReviewVo reviewVo = new ReviewVo(review, reviewRecommendService.getRcdCountByReview(review));
        reviewService.updateCnt(vseq);

        int mseq = review.getMember().getMseq();
        boolean recommendChecked = reviewRecommendService.checkReviewRecommend(mseq, vseq);


        float score = review.getLikes();

        // 모델에 게시글 추가
        model.addAttribute("reviewVo", reviewVo);
        model.addAttribute("authorList", reviewService.getAuthorReviewVoList(mseq));

        // 게시글의 작성자와 현재 사용자가 같은지 확인하여 모델에 추가
        model.addAttribute("isAuthor", review.getMember().getMseq() == member.getMseq());

        ReviewScanVo reviewScanVo = (ReviewScanVo) session.getAttribute("reviewScanVo");
        model.addAttribute("reviewScanVo", reviewScanVo);

        //추천게시글인지 확인
        model.addAttribute("recommendChecked",recommendChecked);

        //평점 추가
        model.addAttribute("starScore", score);

        // 게시글 상세보기 페이지로 이동
        return "review/reviewDetail";
    }


    // 게시글 삭제하기
    @PostMapping("/delete/{vseq}")
    public String reviewDelete(@PathVariable("vseq") int vseq, HttpSession session, HttpServletRequest request,
                              Model model) {

        // 세션에서 사용자 정보 가져오기
        Member member = (Member) session.getAttribute("loginUser");
        // MemberVo memberVo = new MemberVo(member);
        // 세션에 로그인 정보가 없는 경우
        if (member == null) {
            // 로그인 알림을 포함한 경고 메시지를 설정합니다.
            model.addAttribute("msg","로그인 후 이용해주세요.");
            model.addAttribute("redirectTo","/");
            return "review/review_alert";
        }
        // model.addAttribute("memberVo", memberVo);
        reviewCommentService.deletAllComment(vseq);
        reviewService.deleteReview(vseq);
        List<Integer> list = new ArrayList<>();
        list.add(vseq);
        dataService.reviewListDeleteInCsv(list);

        return "redirect:/review/list";

    }


    // 게시글 수정화면으로 이동하기
    @GetMapping("/edit_form/{vseq}")
    public String reviewEditGo(@PathVariable("vseq") int vseq, Model model, HttpSession session, HttpServletRequest request) {

        // 세션에서 사용자 정보 가져오기
        Member member = (Member) session.getAttribute("loginUser");
        // MemberVo memberVo = new MemberVo(member);
        // 세션에 로그인 정보가 없는 경우
        if (member == null) {
            // 로그인 알림을 포함한 경고 메시지를 설정합니다.
            model.addAttribute("msg","로그인 후 이용해주세요.");
            model.addAttribute("redirectTo","/");
            return "review/review_alert";
        }

        // 게시글 번호를 통해 해당 게시글 가져오기
        Review review = reviewService.getReview(vseq);
        // 모델에 게시글 추가
        // model.addAttribute("memberVo", memberVo);
        model.addAttribute("review", review);
        // 게시글 수정화면으로 이동
        return "review/reviewEdit";
    }

    //게시글 수정하기
    @PostMapping("/edit")
    public String reviewEdit(@RequestParam("title") String title,
                            @RequestParam("content") String content,
                            @RequestParam("vseq") int vseq,
                            @RequestParam("cseq") int cseq,
                            @RequestParam("likes") float likes,
                            @RequestParam("cnt") int cnt,
                            HttpSession session, HttpServletRequest request, Model model) {

        // 세션에서 사용자 정보 가져오기
        Member member = (Member) session.getAttribute("loginUser");
        // MemberVo memberVo = new MemberVo(member);
        // 세션에 로그인 정보가 없는 경우
        if (member == null) {
            // 로그인 알림을 포함한 경고 메시지를 설정합니다.
            model.addAttribute("msg","로그인 후 이용해주세요.");
            model.addAttribute("redirectTo","/");
            return "review/review_alert";
        }
        Camp camp = new Camp();
        camp.setCseq(cseq);
        Review vo = new Review();
        vo.setVseq(vseq);
        vo.setTitle(title);
        vo.setContent(content);
        vo.setMember(member);
        vo.setLikes(likes);
        vo.setCount(cnt);
        vo.setCamp(camp);
        // model.addAttribute("memberVo", memberVo);

        reviewService.editReview(vo);
        reviewCommentService.updateCommentCount(vseq);
        List<Review> list = new ArrayList<>();
        list.add(vo);
        dataService.reviewListOutToCsv(list);
        return "redirect:/review/list"; // 저장 후 리스트 페이지로 리다이렉트합니다.
    }


    @GetMapping("/memberList/{mseq}")
    public String showReviewList(Model model,
                                 @PathVariable(value = "mseq") int mseq,
                                 HttpSession session) {

        List<ReviewVo> reviewList = reviewService.getAuthorReviewVoList(mseq);
        // 세션에서 사용자 정보 가져오기
        Member member = (Member) session.getAttribute("loginUser");
        // MemberVo memberVo = new MemberVo(member);
        // 세션에 로그인 정보가 없는 경우
        if (member == null) {
            // 로그인 알림을 포함한 경고 메시지를 설정합니다.
            model.addAttribute("msg","로그인 후 이용해주세요.");
            model.addAttribute("redirectTo","/");
            return "review/review_alert";
        }

        model.addAttribute("authorList",reviewList);
            return "review/reviewMemberList";
    }


    @GetMapping("/memberReviewList/{mseq}")
    @ResponseBody
    public ResponseEntity <Map<String, Object>> showReviewList(@PathVariable(value = "mseq") int mseq,
                                              @RequestParam(name = "page", defaultValue = "1") int page,
                                              Model model) {
        int pageSize = 6;

        List<Review> reviewList = reviewService.getAuthorReviewVoList(mseq, page, pageSize);

        System.out.println(reviewList);

        long totalReviews = reviewService.getAuthorReviewVoList(mseq).size();
        int maxPage = (int) Math.ceil((double) totalReviews / pageSize);

        System.out.println(maxPage);
        System.out.println(totalReviews);
        // HTML 문자열 생성
        StringBuilder reviewListHtml = new StringBuilder();

        if (reviewList.isEmpty()) {
            reviewListHtml.append("<p> 작성한 리뷰가 없습니다.</p>");
        } else {
            for (Review review : reviewList) {
                reviewListHtml.append("<tr>");
                reviewListHtml.append("<td><a href='/review/detail/")
                        .append(review.getVseq())
                        .append("'>")
                        .append(review.getTitle())
                        .append(" [")
                        .append(review.getCommentCount())
                        .append("]</a></td>");
                reviewListHtml.append("<td>").append(review.getMember().getNickname()).append("</td>");
                reviewListHtml.append("<td>").append(new SimpleDateFormat("yyyy-MM-dd").format(review.getCreatedAt())).append("</td>");
                reviewListHtml.append("<td>").append(review.getCount()).append("</td>");
                reviewListHtml.append("</tr>");
            }
        }

        Map<String, Object> response = new HashMap<>();
        response.put("reviewListHtml", reviewListHtml.toString());
        response.put("currentPage", page);
        response.put("maxPage", maxPage);


        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/memberCommentList/{mseq}")
    @ResponseBody
    public ResponseEntity <Map<String, Object>> showReviewCommentList(@PathVariable(value = "mseq") int mseq,
                                                               @RequestParam(name = "page", defaultValue = "1") int page,
                                                               Model model) {
        int pageSize = 6;

        List<ReviewComment> reviewCommnetList = reviewCommentService.getAuthorReviewCommentList(mseq, page, pageSize);

        System.out.println(reviewCommnetList);

        long totalReviews = reviewCommentService.getCommentMemberList(mseq).size();
        int maxPage = (int) Math.ceil((double) totalReviews / pageSize);

        System.out.println(maxPage);
        System.out.println(totalReviews);
        // HTML 문자열 생성
        StringBuilder commentListHtml = new StringBuilder();

        if (reviewCommnetList.isEmpty()) {
            commentListHtml.append("<p> 작성한 댓글이 없습니다.</p>");
        } else {
            for (ReviewComment reviewComment : reviewCommnetList) {
                commentListHtml.append("<tr>");
                commentListHtml.append("<td><a href='/review/detail/")
                        .append(reviewComment.getReview().getVseq())
                        .append("'>")
                        .append(reviewComment.getContent())
                        .append("</a></td>");
                commentListHtml.append("<td>").append(reviewComment.getMember().getNickname()).append("</td>");
                commentListHtml.append("<td>").append(new SimpleDateFormat("yyyy-MM-dd").format(reviewComment.getCreatedAt())).append("</td>");
                commentListHtml.append("</tr>");
            }
        }

        Map<String, Object> response = new HashMap<>();
        response.put("commentListHtml", commentListHtml.toString());
        response.put("currentPage", page);
        response.put("maxPage", maxPage);


        return ResponseEntity.ok().body(response);
    }


    @PostMapping("/reloadRating")
    @ResponseBody
    public Map<String, Object> reloadRating(HttpSession session,
                                            @RequestParam(value="current") float current,
                                            @RequestParam(value="index") int index) {
        Map<String, Object> result = new HashMap<>();
        Member member = (Member) session.getAttribute("loginUser");
        if (member != null) {
            if (index == 1) {
                if (current <= 0.0f) {
                    current = 0.5f;
                } else if (current == 0.5f) {
                    current = 1.0f;
                } else if (current == 1.0f) {
                    current = 0.5f;
                } else if (current > 1.0f) {
                    current = 1.0f;
                }
            } else if (index == 2) {
                if (current <= 1.0f) {
                    current = 1.5f;
                } else if (current == 1.5f) {
                    current = 2.0f;
                } else if (current == 2.0f) {
                    current = 1.5f;
                } else if (current > 2.0f) {
                    current = 2.0f;
                }
            } else if (index == 3) {
                if (current <= 2.0f) {
                    current = 2.5f;
                } else if (current == 2.5f) {
                    current = 3.0f;
                } else if (current == 3.0f) {
                    current = 2.5f;
                } else if (current > 3.0f) {
                    current = 3.0f;
                }
            } else if (index == 4) {
                if (current <= 3.0f) {
                    current = 3.5f;
                } else if (current == 3.5f) {
                    current = 4.0f;
                } else if (current == 4.0f) {
                    current = 3.5f;
                } else if (current > 4.0f) {
                    current = 4.0f;
                }
            } else if (index == 5) {
                if (current <= 4.0f) {
                    current = 4.5f;
                } else if (current == 4.5f) {
                    current = 5.0f;
                } else if (current == 5.0f) {
                    current = 4.5f;
                }
            }
            result.put("starScore", current);
            result.put("result", "success");
        } else {
            result.put("result", "fail");
        }

        return result;
    }

    @PostMapping("/reloadList")
    @ResponseBody
    public Map<String, Object> reloadList(HttpSession session,
                                          @RequestParam(value="page") int page,
                                          @RequestParam(value="sortBy") String sortBy,
                                          @RequestParam(value="sortDirection") String sortDirection) {
        Map<String, Object> result = new HashMap<>();
        Member member = (Member) session.getAttribute("loginUser");
        if (member != null) {
            ReviewScanVo reviewScanVo = (ReviewScanVo) session.getAttribute("reviewScanVo");
            if (reviewScanVo.getPage() != page) {
                reviewScanVo.setPage(page);
                reviewScanVo.setSortBy(sortBy);
                reviewScanVo.setSortDirection(sortDirection);
            }

            result.put("reviewVoList", reviewScanVo.getReviewVoList());
            result.put("reviewVoBestList", reviewScanVo.getReviewVoBestList());
            result.put("totalPages", reviewScanVo.getTotalPages());
            result.put("page", reviewScanVo.getPage());
            result.put("size", reviewScanVo.getSize());
            result.put("pageMaxDisplay", reviewScanVo.getPageMaxDisplay());
            result.put("result", "success");
        } else {
            result.put("result", "fail");
        }

        return result;
    }

    @PostMapping("/recommend/{vseq}")
    @ResponseBody
    public ResponseEntity<?> addToJjimlist(@PathVariable("vseq") int vseq, HttpSession session){

        Member member = (Member) session.getAttribute("loginUser");
        if (member == null) {
            // Return a response entity with a message indicating that the user needs to log in
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인 후 이용해주세요.");
        } else {
            try {
                // Create a new Camp and Recommend object
                Review review = new Review();
                review.setVseq(vseq);

                ReviewRecommend reviewRecommend = new ReviewRecommend();
                reviewRecommend.setReview(review);
                reviewRecommend.setMember(member);

                // 리뷰 추천하기 저장
                reviewRecommendService.addReviewRecommend(reviewRecommend);

                // Return success response
                return ResponseEntity.ok().build();
            } catch (Exception e) {
                // Return error response in case of an exception
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("찜하기에 실패했습니다.");
            }
        }
    }

    @PostMapping("/recommend/delete/{vseq}")
    @ResponseBody
    public ResponseEntity<Void> removeReviewRecommend(@PathVariable("vseq") int vseq, HttpSession session) {

        try {

            Member member = (Member) session.getAttribute("loginUser");
            int mseq = member.getMseq();
            reviewRecommendService.removeReviewRecommend(mseq, vseq);

            return ResponseEntity.ok().build();

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
