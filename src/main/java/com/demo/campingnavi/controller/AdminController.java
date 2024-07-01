package com.demo.campingnavi.controller;

import com.demo.campingnavi.config.PathConfig;
import com.demo.campingnavi.domain.*;
import com.demo.campingnavi.dto.MemberVo;
import com.demo.campingnavi.dto.ReviewScanVo;
import com.demo.campingnavi.dto.ReviewVo;
import com.demo.campingnavi.repository.jpa.UpdateHistoryRepository;
import com.demo.campingnavi.service.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private AdminService adminService;

    @Autowired
    ReviewService reviewService;

    @Autowired
    ReviewRecommendService reviewRecommendService;

    @Autowired
    ReviewCommentService reviewCommentService;

    @Autowired
    CampService campService;

    @Autowired
    DataService dataService;

    @Autowired
    UpdateHistoryService updateHistoryService;

    @Autowired
    private MemberService memberService;
    @Autowired
    private UpdateHistoryRepository updateHistoryRepository;

    @Autowired
    private QnaService qnaService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/")
    public String adminIndex() {
        return "admin/adminPage";
    }

    @GetMapping("/login")
    public String adminLogin() {
        return "admin/loginPage";
    }

    @PostMapping("/loginProc")
    public String adminLoginProc(MemberVo vo, HttpSession session, Model model) {
        Member admin = memberService.findByUsername(vo.getUsername());
        if (admin != null) {
            if (!admin.getRole().equals(Role.USER.getKey()) && admin.getRole() != null) {
                if (passwordEncoder.matches(vo.getPw(), admin.getPw())) {
                    session.setAttribute("admin", admin);
                    return "redirect:/admin/";
                } else {
                    model.addAttribute("msg", "비밀번호가 다릅니다.");
                    return "admin/loginPage";
                }
            } else {
                model.addAttribute("msg", "관리자가 아닙니다.");
                return "admin/loginPage";
            }
        } else {
            model.addAttribute("msg", "일치하는 사용자가 없습니다.");
            return "admin/loginPage";
        }
    }

    @Transactional
    @ResponseBody
    @PostMapping("/update")
    public Map<String, Object> recommendModelUpdate(@RequestParam("kind") String kind) {
        Map<String, Object> result = new HashMap<>();
        String text = "";
        if (kind.equals("model")) {
            text = adminService.recommendModelUpdate();
        } else {
            // 캠프 데이터 최신화
            text = adminService.campDataUpdate();
            // 기존 데이터베이스 캠프를 모두 n으로 처리
            campService.campAllDisabled();
            // 최신화된 데이터를 새로 적용
            String csvFile = "campingData.csv";
            String n = "all";
            List<Camp> campList = dataService.campInFromCsv(csvFile, n);
        }
        result.put("result", text);

        UpdateHistory updateHistory = new UpdateHistory();
        updateHistory.setKind(kind);
        updateHistory.setResult(text);
        updateHistoryService.saveUpdateHistory(updateHistory);

        return result;
    }

    @ResponseBody
    @PostMapping("/get_totalCount")
    public Map<String, Object> getTotalCount(@RequestParam("kind") String kind) {
        Map<String, Object> result = new HashMap<>();
        String text = "";
        int totalCount = adminService.getCampingTotalCount();
        try {
            if (totalCount >= 0) {
                text = "success";
                result.put("result", text);
                result.put("totalCount", totalCount);
            } else {
                text = "fail";
                result.put("result", text);
                UpdateHistory updateHistory = new UpdateHistory();
                updateHistory.setKind(kind);
                updateHistory.setResult(text);
                updateHistoryService.saveUpdateHistory(updateHistory);
            }
        } catch (Exception e) {
            e.printStackTrace();
            text = "fail";
            result.put("result", text);
            UpdateHistory updateHistory = new UpdateHistory();
            updateHistory.setKind(kind);
            updateHistory.setResult(text);
            updateHistoryService.saveUpdateHistory(updateHistory);
        }

        return result;
    }

    @ResponseBody
    @PostMapping("/camping_data_search_from_api")
    public Map<String, Object> campingDataSearchFromApi(@RequestParam("kind") String kind,
                                                        @RequestParam("page") int page) {
        Map<String, Object> result = new HashMap<>();
        String text = "";
        try {
            text = adminService.getCampingDataFromApi(page);
            if (text.equals("success")) {
                result.put("result", text);
            } else {
                result.put("result", text);
                UpdateHistory updateHistory = new UpdateHistory();
                updateHistory.setKind(kind);
                updateHistory.setResult(text);
                updateHistoryService.saveUpdateHistory(updateHistory);
            }
        } catch (Exception e) {
            e.printStackTrace();
            text = "fail";
            result.put("result", text);
            UpdateHistory updateHistory = new UpdateHistory();
            updateHistory.setKind(kind);
            updateHistory.setResult(text);
            updateHistoryService.saveUpdateHistory(updateHistory);
        }
        return result;
    }

    @ResponseBody
    @PostMapping("/camping_data_integration")
    public Map<String, Object> campingDataIntegration(@RequestParam("kind") String kind,
                                                      @RequestParam("totalPage") int totalPage) {
        Map<String, Object> result = new HashMap<>();
        String text = "";
        try {
            text = adminService.getCampingDataIntegration(totalPage);
            if (text.equals("success")) {
                result.put("result", text);
            } else {
                result.put("result", text);
                UpdateHistory updateHistory = new UpdateHistory();
                updateHistory.setKind(kind);
                updateHistory.setResult(text);
                updateHistoryService.saveUpdateHistory(updateHistory);
            }
        } catch (Exception e) {
            e.printStackTrace();
            text = "fail";
            result.put("result", text);
            UpdateHistory updateHistory = new UpdateHistory();
            updateHistory.setKind(kind);
            updateHistory.setResult(text);
            updateHistoryService.saveUpdateHistory(updateHistory);
        }

        return result;
    }

    @Transactional
    @ResponseBody
    @PostMapping("/camping_data_insert")
    public Map<String, Object> campingDataInsert(@RequestParam("kind") String kind) {
        Map<String, Object> result = new HashMap<>();
        String text = "";
        try {
            // 기존 데이터베이스 캠프를 모두 n으로 처리
            campService.campAllDisabled();
            // 최신화된 데이터를 새로 적용
            String csvFile = "campingData.csv";
            String n = "all";
            List<Camp> campList = dataService.campInFromCsv(csvFile, n);
            text = "success";
            result.put("result", text);
            UpdateHistory updateHistory = new UpdateHistory();
            updateHistory.setKind(kind);
            updateHistory.setResult(text);
            updateHistoryService.saveUpdateHistory(updateHistory);
        } catch (Exception e) {
            e.printStackTrace();
            text = "fail";
            result.put("result", text);
            UpdateHistory updateHistory = new UpdateHistory();
            updateHistory.setKind(kind);
            updateHistory.setResult(text);
            updateHistoryService.saveUpdateHistory(updateHistory);
        }

        result.put("result", text);
        return result;
    }

    @ResponseBody
    @PostMapping("/crawling_initialize")
    public Map<String, Object> crawlingInitialize() {
        Map<String, Object> result = new HashMap<>();
        String update_type = "start";
        dataService.deleteFile("/temp/crawling_stop");
        List<UpdateHistory> updateListCampSuccess = updateHistoryService.getUpdateHistoryList("camp", "success");
        List<UpdateHistory> updateListCrawlingStopped = updateHistoryService.getUpdateHistoryList("crawling", "stopped");
        List<UpdateHistory> updateListCrawlingSuccess = updateHistoryService.getUpdateHistoryList("crawling", "success");

        if (updateListCampSuccess.isEmpty()) {
            update_type = "fail";
        } else if (updateListCrawlingSuccess.size() + updateListCrawlingStopped.size() == 0) {
            update_type = "start";
        } else {
            if (updateListCrawlingStopped.isEmpty()) {
                update_type = "start";
            } else {
                if (updateListCrawlingStopped.get(updateListCrawlingStopped.size() - 1).getUseq() < updateListCampSuccess.get(updateListCampSuccess.size() - 1).getUseq()) {
                    update_type = "start";
                } else if (updateListCrawlingSuccess.isEmpty()) {
                    update_type = "resume";
                } else {
                    if (updateListCrawlingStopped.get(updateListCrawlingStopped.size() - 1).getUseq() < updateListCrawlingSuccess.get(updateListCrawlingSuccess.size() - 1).getUseq()) {
                        update_type = "start";
                    } else {
                        update_type = "resume";
                    }
                }
            }
        }

        if (update_type.equals("start")) {
            try {
                String delete_result = dataService.deleteFile("/temp/crawling_status.csv");
                if (delete_result.equals("fail")) {
                    update_type = "fail";
                }
            } catch(Exception e) {
                update_type = "fail";
                e.printStackTrace();
            }
        } else if (update_type.equals("fail")) {
            UpdateHistory updateHistory = new UpdateHistory();
            updateHistory.setKind("crawling");
            updateHistory.setResult("fail");
            updateHistoryService.saveUpdateHistory(updateHistory);
        }

        result.put("result", update_type);
        if (update_type.equals("start")) {
            adminService.clearRatingTempFile(1);
        }

        return result;
    }

    @ResponseBody
    @PostMapping("/get_crawling_data")
    public Map<String, Object> getCrawlingData() {
        Map<String, Object> result = new HashMap<>();
        String text = "";
        try {
            text = adminService.getCrawlingData();
        } catch (Exception e) {
            e.printStackTrace();
            text = "fail";
        }

        if (text.equals("fail")) {
            UpdateHistory updateHistory = new UpdateHistory();
            updateHistory.setKind("crawling");
            updateHistory.setResult("fail");
            updateHistoryService.saveUpdateHistory(updateHistory);
        }

        result.put("result", text);

        return result;
    }

    @ResponseBody
    @PostMapping("/crawling_data_integration")
    public Map<String, Object> crawlingDataIntegration() {
        Map<String, Object> result = new HashMap<>();
        String text = "";
        try {
            text = adminService.getCrawlingDataIntegration();
            if (text.equals("success")) {
                result.put("result", text);
            } else {
                result.put("result", text);
                UpdateHistory updateHistory = new UpdateHistory();
                updateHistory.setKind("crawling");
                updateHistory.setResult(text);
                updateHistoryService.saveUpdateHistory(updateHistory);
            }
        } catch (Exception e) {
            e.printStackTrace();
            text = "fail";
            result.put("result", text);
            UpdateHistory updateHistory = new UpdateHistory();
            updateHistory.setKind("crawling");
            updateHistory.setResult(text);
            updateHistoryService.saveUpdateHistory(updateHistory);
        }

        return result;
    }

    @ResponseBody
    @PostMapping("/crawling_stop")
    public Map<String, Object> crawlingDataStop(HttpSession session) {
        Map<String, Object> result = new HashMap<>();
        String text = "";
        try {
            text = dataService.createFile("/temp/crawling_stop");
            if (text.equals("success")) {
                result.put("result", text);
                UpdateHistory updateHistory = new UpdateHistory();
                updateHistory.setKind("crawling");
                updateHistory.setResult("stopped");
                updateHistoryService.saveUpdateHistory(updateHistory);
                session.setAttribute("crawling", "n");
            } else {
                result.put("result", text);
            }
        } catch (Exception e) {
            e.printStackTrace();
            text = "fail";
            result.put("result", text);
        }

        return result;
    }

    @ResponseBody
    @PostMapping("/set_crawling_session")
    public Map<String, Object> clearCrawlingSession(HttpSession session,
                                                    @RequestParam("value") String value) {
        Map<String, Object> result = new HashMap<>();
        session.setAttribute("crawling", value);
        if (value.equals("y")) {
            result.put("result", "open");
        } else {
            result.put("result", "close");
        }
        return result;
    }

    @ResponseBody
    @PostMapping("/load_update_history")
    public Map<String, Object> loadUpdateHistory(HttpSession session, @RequestParam(value = "kind") String kind) {
        Map<String, Object> result = new HashMap<>();
        List<UpdateHistory> updateHistoryListSuccess = updateHistoryService.getUpdateHistoryList(kind, "success");
        List<UpdateHistory> updateHistoryListFail = updateHistoryService.getUpdateHistoryList(kind, "fail");
        List<UpdateHistory> updateHistoryListStopped = updateHistoryService.getUpdateHistoryList(kind, "stopped");
        Map<String, Integer> crawlingStatus = adminService.getCrawlingStatus();

        UpdateHistory updateHistorySuccess = null;
        int updateHistorySuccessUseq = 0;
        if (!updateHistoryListSuccess.isEmpty()) {
            updateHistorySuccess = updateHistoryListSuccess.get(updateHistoryListSuccess.size() - 1);
            updateHistorySuccessUseq = updateHistorySuccess.getUseq();
        }

        UpdateHistory updateHistoryFail = null;
        int updateHistoryFailUseq = 0;
        if (!updateHistoryListFail.isEmpty()) {
            updateHistoryFail = updateHistoryListFail.get(updateHistoryListFail.size() - 1);
            updateHistoryFailUseq = updateHistoryFail.getUseq();
        }

        UpdateHistory updateHistoryStopped = null;
        int updateHistoryStoppedUseq = 0;
        if (!updateHistoryListStopped.isEmpty()) {
            updateHistoryStopped = updateHistoryListStopped.get(updateHistoryListStopped.size() - 1);
            updateHistoryStoppedUseq = updateHistoryStopped.getUseq();
        }
        String defaultText = "내역 없음";
        String updateTime = new String(defaultText);
        String updateTry = new String(defaultText);
        String text = "";
        if (!kind.equals("crawling")) {
            if (updateHistorySuccessUseq > 0) {
                updateTime = updateHistorySuccess.getUpdateTime().toString().substring(0, 19);
            }
            if (updateHistorySuccessUseq > updateHistoryFailUseq) {
                updateTry = updateTime;
                text = "success";
            } else {
                if (updateHistoryFailUseq > 0) {
                    updateTry = updateHistoryFail.getUpdateTime().toString().substring(0, 19);
                    text = "fail";
                }
            }
        } else {
            if (updateHistorySuccessUseq > 0) {
                updateTime = updateHistorySuccess.getUpdateTime().toString().substring(0, 19);
            }
            if (updateHistorySuccessUseq > updateHistoryFailUseq && updateHistorySuccessUseq > updateHistoryStoppedUseq) {
                updateTry = updateTime;
                text = "success";
            } else if (updateHistoryStoppedUseq > updateHistorySuccessUseq && updateHistoryStoppedUseq > updateHistoryFailUseq) {
                updateTry = updateHistoryStopped.getUpdateTime().toString().substring(0, 19);
                text = "stopped";
            } else if (updateHistoryFailUseq > updateHistorySuccessUseq && updateHistoryFailUseq > updateHistoryStoppedUseq) {
                updateTry = updateHistoryFail.getUpdateTime().toString().substring(0, 19);
                text = "fail";
            } else if (String.valueOf(session.getAttribute("crawling")).equals("y")) {
                text = "stopped";
            }
        }

        if (text.equals("")) {
            text = " ";
        } else if (text.equals("success")) {
            text = "성공";
        } else if (text.equals("fail")) {
            text = "실패";
        } else {
            text = "중단";
        }

        if (kind.equals("camp")) {
            List<Camp> campList = campService.getCampListByUseyn("y");
            result.put("totalCount", campList.size());
        } else if (kind.equals("crawling")) {
            result.put("totalCount", crawlingStatus.get("total"));
            result.put("currentCount", crawlingStatus.get("current"));
        }

        result.put("updateTime", updateTime);
        result.put("updateTry", updateTry);
        result.put("result", text);
        return result;
    }

    @GetMapping("/update_page")
    public String updatePage(HttpSession session, Model model) {
        String crawling = "n";
        if (session.getAttribute("crawling") != null) {
            crawling = (String) session.getAttribute("crawling");
        }
        session.setAttribute("crawling", crawling);
        model.addAttribute("crawling", crawling);

        String dir = "/temp";
        dataService.createDir(dir);

        return "admin/update/adminUpdate";
    }


    // 리뷰관리 리스트 보기
    @GetMapping("/review/list")
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

        // 세션에서 어드민 정보 가져오기
        //Admin admin = (Admin) session.getAttribute("adminUser");
        // 세션에 로그인 정보가 없는 경우
//        if (admin == null) {
//            // 로그인 알림을 포함한 경고 메시지를 설정합니다.
//            model.addAttribute("msg","관리자 로그인 후 이용해주세요.");
//            model.addAttribute("redirectTo","/");
//            return "review/review_alert";
//        }

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
            reviewScanVo.setTotalPages((reviewScanVo.getReviewVoList().size() + reviewScanVo.getSize() - 1) / reviewScanVo.getSize());
            session.setAttribute("reviewScanVo", reviewScanVo);
        } else {
            reviewScanVo = (ReviewScanVo) session.getAttribute("reviewScanVo");
            reviewScanVo.setPage(page);
            List<ReviewVo> reviewVoList = reviewService.findReviewVoList(reviewScanVo);
            reviewScanVo.setReviewVoList(reviewVoList);
            reviewScanVo.setReviewVoBestList(reviewService.getBestReviewVoList());
            reviewScanVo.setTotalPages((reviewScanVo.getReviewVoList().size() + reviewScanVo.getSize() - 1) / reviewScanVo.getSize());
            session.setAttribute("reviewScanVo", reviewScanVo);
        }

        reviewScanVo = (ReviewScanVo) session.getAttribute("reviewScanVo");
        model.addAttribute("reviewScanVo", reviewScanVo);

        return "/admin/review/adminReviewList";
    }

    // 리뷰 상세보기
    @GetMapping("/review/view/{vseq}")
    public String reviewDetail(@PathVariable("vseq") int vseq, Model model, HttpSession session) {

        // 세션에서 어드민 정보 가져오기
        //Admin admin = (Admin) session.getAttribute("adminUser");
        // 세션에 로그인 정보가 없는 경우
//        if (admin == null) {
//            // 로그인 알림을 포함한 경고 메시지를 설정합니다.
//            model.addAttribute("msg","관리자 로그인 후 이용해주세요.");
//            model.addAttribute("redirectTo","/");
//            return "review/review_alert";
//        }

        // 게시글 번호를 통해 해당 게시글 가져오기
        Review review = reviewService.getReview(vseq);
        ReviewVo reviewVo = new ReviewVo(review, reviewRecommendService.getRcdCountByReview(review));
       
        float score = review.getLikes();

        // 모델에 게시글 추가
        model.addAttribute("reviewVo", reviewVo);

        ReviewScanVo reviewScanVo = (ReviewScanVo) session.getAttribute("reviewScanVo");
        model.addAttribute("reviewScanVo", reviewScanVo);

        //평점 추가
        model.addAttribute("starScore", score);

        // 게시글 상세보기 페이지로 이동
        return "/admin/review/adminReviewDetail";
    }

    // 리뷰 삭제하기
    @PostMapping("/review/delete/{vseq}")
    public String reviewDelete(@PathVariable("vseq") int vseq, HttpSession session, HttpServletRequest request,
                               Model model) {

        // 세션에서 어드민 정보 가져오기
        //Admin admin = (Admin) session.getAttribute("adminUser");
        // 세션에 로그인 정보가 없는 경우
//        if (admin == null) {
//            // 로그인 알림을 포함한 경고 메시지를 설정합니다.
//            model.addAttribute("msg","관리자 로그인 후 이용해주세요.");
//            model.addAttribute("redirectTo","/");
//            return "review/review_alert";
//        }
        reviewCommentService.deletAllComment(vseq);
        reviewService.deleteReview(vseq);

        return "redirect:/admin/review/list";

    }


    @PostMapping("/review/reloadList")
    @ResponseBody
    public Map<String, Object> reloadList(HttpSession session,
                                          @RequestParam(value = "page") int page,
                                          @RequestParam(value = "sortBy") String sortBy,
                                          @RequestParam(value = "sortDirection") String sortDirection) {
        Map<String, Object> result = new HashMap<>();
        // 세션에서 어드민 정보 가져오기
        //Admin admin = (Admin) session.getAttribute("adminUser");
        // if (admin != null) {
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
        //} else {
        // result.put("result", "fail");
        // }

        return result;
    }

    @GetMapping(value = "/review/comment/list")
    @ResponseBody
    public Map<String, Object> getComments(@RequestParam(value = "vseq") int vseq, HttpSession session) {
        Map<String, Object> result = new HashMap<>();

        // 댓글 목록 가져오기
        List<ReviewComment> parentComments = reviewCommentService.getCommentList(vseq);
        int[] parentCommentCmseqArray = new int[parentComments.size()];
        int[] parentCommentMseqArray = new int[parentComments.size()];
        String[] parentCommentMemberArray = new String[parentComments.size()];
        String[] parentCommentContentArray = new String[parentComments.size()];
        String[] parentCommentDateArray = new String[parentComments.size()];

        for (int i = 0; i < parentComments.size(); i++) {
            parentCommentCmseqArray[i] = parentComments.get(i).getCmseq();
            parentCommentMseqArray[i] = parentComments.get(i).getMember().getMseq();


            Member tmp_member = parentComments.get(i).getMember();

            parentCommentMemberArray[i] = tmp_member.getName() + "(" + tmp_member.getUsername() + ")";
            parentCommentContentArray[i] = parentComments.get(i).getContent();
            String date = parentComments.get(i).getCreatedAt().toString();
            parentCommentDateArray[i] = date;
        }


        int[][] childComentCmseqArray = new int[parentComments.size()][];
        int[][] childCommentMseqArray = new int[parentComments.size()][];
        String[][] childCommentMemberArray = new String[parentComments.size()][];
        String[][] childCommentContentArray = new String[parentComments.size()][];
        String[][] childCommentDateArray = new String[parentComments.size()][];

        int[] tmp_CmseqArray = null;
        int[] tmp_MseqArray = null;
        String[] tmp_MemberArray = null;
        String[] tmp_ContentArray = null;
        String[] tmp_DateArray = null;
        // 부모 댓글마다 대댓글 목록 가져오기
        for (int i = 0; i < parentComments.size(); i++) {
            List<ReviewComment> replies = reviewCommentService.getReplyCommentList(parentComments.get(i).getCmseq());
            tmp_CmseqArray = new int[replies.size()];
            tmp_MseqArray = new int[replies.size()];
            tmp_MemberArray = new String[replies.size()];
            tmp_ContentArray = new String[replies.size()];
            tmp_DateArray = new String[replies.size()];
            for (int j = 0; j < replies.size(); j++) {
                tmp_CmseqArray[j] = replies.get(j).getCmseq();
                tmp_MseqArray[j] = replies.get(j).getMember().getMseq();

                Member tmp_member = replies.get(j).getMember();

                tmp_MemberArray[j] = tmp_member.getName() + "(" + tmp_member.getUsername() + ")";
                tmp_ContentArray[j] = replies.get(j).getContent();
                String date = replies.get(j).getCreatedAt().toString();
                // date = date.substring(0, date.length()-4);
                tmp_DateArray[j] = date;
            }
            childComentCmseqArray[i] = tmp_CmseqArray;
            childCommentMseqArray[i] = tmp_MseqArray;
            childCommentMemberArray[i] = tmp_MemberArray;
            childCommentContentArray[i] = tmp_ContentArray;
            childCommentDateArray[i] = tmp_DateArray;
        }

        // 대댓글을 포함한 댓글 수 계산
        int totalCommentCount = reviewService.getReview(vseq).getCommentCount();

        result.put("commentCount", totalCommentCount); // 대댓글을 포함한 총 댓글 수를 전달
        result.put("parentCommentCmseqArray", parentCommentCmseqArray);
        result.put("parentCommentMseqArray", parentCommentMseqArray);
        result.put("parentCommentMemberArray", parentCommentMemberArray);
        result.put("parentCommentContentArray", parentCommentContentArray);
        result.put("parentCommentDateArray", parentCommentDateArray);
        result.put("childCommentCmseqArray", childComentCmseqArray);
        result.put("childCommentMseqArray", childCommentMseqArray);
        result.put("childCommentMemberArray", childCommentMemberArray);
        result.put("childCommentContentArray", childCommentContentArray);
        result.put("childCommentDateArray", childCommentDateArray);

        return result;
    }

    // 댓글 삭제
    @PostMapping(value = "/review/comment/delete")
    @ResponseBody
    public Map<String, Object> deleteCommentAction(@RequestParam(value = "cmseq") int cmseq) {

        Map<String, Object> map = new HashMap<>();

        reviewCommentService.deletComment(cmseq);
        map.put("result", "success");

        return map;
    }

    @GetMapping("/member/list")
    public String memberListView() {
        return "admin/member/adminMemberList";
    }

    @GetMapping("/member/list/page")
    @ResponseBody
    public Page<Member> memberPaging(Pageable pageable,
                                     @RequestParam(value = "searchField", defaultValue = "") String searchField,
                                     @RequestParam(value = "searchWord", defaultValue = "") String searchWord) {
        if (searchField.equals("username")) {
            return memberService.findAllByUsername(searchWord, pageable);
        } else if (searchField.equals("nickname")) {
            return memberService.findAllByName(searchWord, pageable);
        } else if (searchField.equals("email")) {
            return memberService.findAllByEmail(searchWord, pageable);
        } else if (searchField.equals("provider")) {
            return memberService.findAllByProvider(searchWord, pageable);
        } else {
            return memberService.findAll(pageable);
        }
    }

    @GetMapping("/member/detail/{mseq}")
    public String memberDetailView(@PathVariable("mseq") int mseq,
                                   Model model) {
        Member member = memberService.findById(mseq);
        model.addAttribute("member", member);
        return "admin/member/adminMemberDetail";
    }


    @PostMapping("/member/delete")
    @ResponseBody
    public Map<String, Object> deleteMember(@RequestParam(value = "mseq") int mseq) {
        Map<String, Object> result = new HashMap<>();

        if (mseq != 0) {
            Member member = memberService.findById(mseq);
            if (!member.getUseyn().equals("n")) {
                member.setUseyn("n");
                memberService.saveMember(member);
                result.put("result", "success");
                result.put("useyn", member.getUseyn());
            } else {
                result.put("result", "할 수 없습니다. 이미 탈퇴한 회원입니다.");
            }

        } else {
            result.put("result", "fail");
        }

        return result;
    }

    @PostMapping("/member/restore")
    @ResponseBody
    public Map<String, Object> restoreMember(@RequestParam(value = "mseq") int mseq) {
        Map<String, Object> result = new HashMap<>();

        if (mseq != 0) {
            Member member = memberService.findById(mseq);
            if (!member.getUseyn().equals("y")) {
                member.setUseyn("y");
                memberService.saveMember(member);
                result.put("result", "success");
                result.put("useyn", member.getUseyn());
            } else {
                result.put("result", "할 수 없습니다. 이미 사용중인 회원입니다.");
            }

        } else {
            result.put("result", "fail");
        }

        return result;
    }

    @GetMapping("/mail")
    public String writeMail() {
        return "admin/member/adminMail";
    }

    @GetMapping("/supervisor/list")
    public String adminListView() {
        return "admin/supervisor/adminList";
    }

    @GetMapping("/supervisor/list/page")
    @ResponseBody
    public Page<Member> adminPaging(Pageable pageable,
                                    @RequestParam(value = "searchField", defaultValue = "") String searchField,
                                    @RequestParam(value = "searchWord", defaultValue = "") String searchWord) {
        if (searchField.equals("username")) {
            return memberService.findAllAdminByUsername(searchWord, pageable);
        } else if (searchField.equals("name")) {
            return memberService.findAllAdminByName(searchWord, pageable);
        } else {
            return memberService.findAllAdmin(pageable);
        }
    }

    @GetMapping("/supervisor/addAdmin")
    public String addAdminView() {
        return "admin/supervisor/addPage";
    }

    @PostMapping("/adminAddProc")
    public String joinProcess(MemberVo vo, Model model) {

        boolean result = memberService.adminAdditionProc(vo);
        if (result) {
            return "admin/supervisor/adminList";
        } else {
            return "admin/supervisor/addPage";
        }
    }

    @GetMapping("/qna/list")
    public String qnaListView() {
        return "admin/qna/adminQnaList";
    }

    @GetMapping("/qna/list/page")
    @ResponseBody
    public Page<Qna> adminQNAPaging(Pageable pageable,
                                       @RequestParam(value = "searchField", defaultValue = "") String searchField,
                                       @RequestParam(value = "searchWord", defaultValue = "") String searchWord,
                                       @RequestParam(value = "type") String type) {
        if (searchField.equals("username")) {
            return qnaService.findAllByUsername(type, searchWord, pageable);
        } else if (searchField.equals("nickname")) {
            return qnaService.findAllByNickName(type, searchWord, pageable);
        } else if (searchField.equals("title")) {
            return qnaService.findAllByTitle(type, searchWord, pageable);
        } else if (searchField.equals("content")) {
            return qnaService.findAllByContent(type, searchWord, pageable);
        } else {
            return qnaService.findAllByTypeExceptNone(type, pageable);
        }
    }

    @PostMapping("/qna/delete")
    @ResponseBody
    public Map<String, Object> deleteQna(@RequestParam(value = "qseq") int qseq) {
        Map<String, Object> result = new HashMap<>();

        if (qseq != 0) {
            Qna qna = qnaService.findById(qseq);
            if (!qna.getUseyn().equals("n")) {
                qna.setUseyn("n");
                qnaService.saveQna(qna);
                result.put("result", "success");
            } else {
                result.put("result", "할 수 없습니다. 이미 삭제된 글입니다.");
            }

        } else {
            result.put("result", "fail");
        }

        return result;
    }

    @PostMapping("/qna/restore")
    @ResponseBody
    public Map<String, Object> restoreQna(@RequestParam(value = "qseq") int qseq) {
        Map<String, Object> result = new HashMap<>();

        if (qseq != 0) {
            Qna qna = qnaService.findById(qseq);
            if (!qna.getUseyn().equals("y")) {
                qna.setUseyn("y");
                qnaService.saveQna(qna);
                result.put("result", "success");
            } else {
                result.put("result", "할 수 없습니다. 이미 사용중인 글입니다.");
            }

        } else {
            result.put("result", "fail");
        }

        return result;
    }

    @GetMapping("/faq/list")
    public String fqaListView() {
        return "admin/qna/adminFaqList";
    }

    @GetMapping("/chat/adminChatList")
    public String adminchatListView(){

        return "admin/chat/adminChatList";
    }
}
