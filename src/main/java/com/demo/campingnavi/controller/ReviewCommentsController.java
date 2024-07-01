package com.demo.campingnavi.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.demo.campingnavi.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.demo.campingnavi.domain.Review;
import com.demo.campingnavi.domain.ReviewComment;
import com.demo.campingnavi.domain.Member;
import com.demo.campingnavi.service.ReviewCommentService;
import com.demo.campingnavi.service.MemberService;

import jakarta.servlet.http.HttpSession;
@RestController
@RequestMapping("/review/comment/")
public class ReviewCommentsController {

	@Autowired
	ReviewCommentService reviewCommentService;
	@Autowired
	ReviewService reviewService;
	@Autowired
	MemberService memberService;

	@GetMapping(value = "/list")
	public Map<String, Object> getComments(@RequestParam(value = "vseq") int vseq, HttpSession session) {
		Map<String, Object> result = new HashMap<>();
		System.out.println(1);
		// 세션에서 사용자 정보 가져오기
		Member member = (Member) session.getAttribute("loginUser");
		int currentMember = member.getMseq();

		// 댓글 목록 가져오기
		List<ReviewComment> parentComments = reviewCommentService.getCommentList(vseq);
		int[] parentCommentCmseqArray = new int[parentComments.size()];
		int[] parentCommentMseqArray = new int[parentComments.size()];
		String[] parentCommentMemberArray = new String[parentComments.size()];
		String[] parentCommentContentArray = new String[parentComments.size()];
		String[] parentCommentDateArray = new String[parentComments.size()];
		
		for (int i = 0 ; i < parentComments.size() ; i++) {
			parentCommentCmseqArray[i] = parentComments.get(i).getCmseq();
			parentCommentMseqArray[i] = parentComments.get(i).getMember().getMseq();

			// tmp_member 임시 땜빵
			// Member tmp_member = member;
			Member tmp_member = parentComments.get(i).getMember();

			parentCommentMemberArray[i] = tmp_member.getName()+"("+tmp_member.getUsername()+")";
			parentCommentContentArray[i] = parentComments.get(i).getContent();
			String date = parentComments.get(i).getCreatedAt().toString();
			// date = date.substring(0, date.length()-4);
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
		for (int i = 0 ; i < parentComments.size() ; i++) {
			List<ReviewComment> replies = reviewCommentService.getReplyCommentList(parentComments.get(i).getCmseq());
			tmp_CmseqArray = new int[replies.size()];
			tmp_MseqArray = new int[replies.size()];
			tmp_MemberArray = new String[replies.size()];
			tmp_ContentArray = new String[replies.size()];
			tmp_DateArray = new String[replies.size()];
			for (int j = 0 ; j < replies.size(); j++) {
				tmp_CmseqArray[j] = replies.get(j).getCmseq();
				tmp_MseqArray[j] = replies.get(j).getMember().getMseq();

				// tmp_member 임시 땜빵
				// Member tmp_member = member;
				Member tmp_member = replies.get(j).getMember();

				tmp_MemberArray[j] = tmp_member.getName()+"("+tmp_member.getUsername()+")";
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

		result.put("currentMember", currentMember);
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



	@PostMapping(value = "/save")
	public Map<String, Object> saveCommentAction(@RequestParam(value = "vseq",required = false) int vseq,
												 @RequestParam(value = "cmseq", required = false) Integer cmseq,
												 @RequestParam(value = "CommentContent", required = false) String content,
												 HttpSession session) {
		Map<String, Object> map = new HashMap<>();
		Member member = (Member) session.getAttribute("loginUser");

		if (member == null) { // 로그인 되어 있지 않음.
			map.put("result", "not_logedin");
		} else {
			if (cmseq == null) {
				// 원댓글 저장
				if (content == null || content.isEmpty()) {
					map.put("result", "fail");
				} else {
					ReviewComment vo = new ReviewComment(); // Comments 객체 생성
					vo.setMember(member);

					Review r = new Review();
					r.setVseq(vseq);
					vo.setReview(r);
					vo.setContent(content);

					try {
						reviewCommentService.saveComment(vo);
						map.put("result", "success");
					} catch (Exception e) {
						map.put("result", "fail");
						e.printStackTrace(); // 에러 로그 출력
					}
				}
			}
			}

		return map;
	}

	@PostMapping(value = "/rplSave")
	public Map<String, Object> saveReplyAction(@RequestParam(value = "vseq", required = false) Integer vseq,
											   @RequestParam(value = "ReplyContent", required = false) String replyContent,
											   @RequestParam(value = "cmseq", required = false) Integer cmseq,
											   HttpSession session) {

		Map<String, Object> map = new HashMap<>();
		Member member = (Member) session.getAttribute("loginUser");

		if (member == null) { // 로그인 되어 있지 않음.
			map.put("result", "not_logedin");
		} else {
			if (replyContent == null || replyContent.isEmpty()) {
				map.put("result", "fail");
			} else {
				ReviewComment vo = new ReviewComment(); // Comments 객체 생성
				vo.setMember(member);

				ReviewComment parentComment = new ReviewComment();
				parentComment.setCmseq(cmseq);
				vo.setParentComment(parentComment);

				Review r = new Review();
				r.setVseq(vseq);
				vo.setReview(r);
				vo.setContent(replyContent);

				try {
					reviewCommentService.saveComment(vo);
					map.put("result", "success");
				} catch (Exception e) {
					map.put("result", "fail");
					e.printStackTrace(); // 에러 로그 출력
				}
			}
		}
		return map;
	}


	// 댓글 삭제
	@PostMapping(value = "/delete")
	public Map<String, Object> deleteCommentAction(@RequestParam(value = "cmseq") int cmseq) {

		Map<String, Object> map = new HashMap<>();

		reviewCommentService.deletComment(cmseq);
		map.put("result", "success");

		return map;
	}
}
