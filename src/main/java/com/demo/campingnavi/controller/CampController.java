package com.demo.campingnavi.controller;

import com.demo.campingnavi.domain.Camp;
import com.demo.campingnavi.domain.Member;
import com.demo.campingnavi.dto.CampRecommendVo;
import com.demo.campingnavi.dto.CampVo;
import com.demo.campingnavi.service.CampService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;

import java.util.*;

@Controller
@RequestMapping("/camp")
public class CampController {

    @Autowired
    private CampService campService;

    @GetMapping("/search")
    public String search(HttpSession session, Model model, CampRecommendVo campRecommendVo,
                         @RequestParam(value="page", defaultValue="0") int page,
                         @RequestParam(value="size", defaultValue="5") int size,
                         @RequestParam(value="sortBy", defaultValue="score") String sortBy,
                         @RequestParam(value="sortDirection", defaultValue="DESC") String sortDirection,
                         @RequestParam(value="pageMaxDisplay", defaultValue="5") int pageMaxDisplay,
                         @RequestParam(value="searchField", defaultValue="name") String searchField,
                         @RequestParam(value="searchWord", defaultValue="") String searchWord,
                         @RequestParam(value="campType", defaultValue="") String[] campType,
                         @RequestParam(value="useyn", defaultValue="y") String useyn) {

        Member member = (Member) session.getAttribute("loginUser");
        System.out.println(member);
        if (member == null) {
            return "redirect:/";
        }

        campRecommendVo.setPage(1);
        campRecommendVo.setSize(size);
        campRecommendVo.setSortBy(sortBy);
        campRecommendVo.setSortDirection(sortDirection);
        campRecommendVo.setPageMaxDisplay(pageMaxDisplay);
        campRecommendVo.setUseyn(useyn);
        campRecommendVo.setSearchField(searchField);
        campRecommendVo.setSearchWord(searchWord);

        List<String> campTypeList = Arrays.asList(campType);
        campType = new String[campRecommendVo.getCampTypeArray().length];
        for (int i = 0 ; i < campRecommendVo.getCampTypeArray().length ; i++) {
            String type = campRecommendVo.getCampTypeArray()[i][0];
            if (campTypeList.contains(type)) {
                campType[i] = type;
            } else {
                campType[i] = "";
            }
        }
        campRecommendVo.setCampType(campType);

        campRecommendVo.setCampList(campService.getCampScanList(campRecommendVo));
        campService.saveCampRecommendList(campRecommendVo.getCampList(), member, campRecommendVo);
        campRecommendVo.setCampRecommendList(campRecommendVo.getCampRecommendListNotVisited());
        campRecommendVo.setTotalPages((campRecommendVo.getCampRecommendList().size()+ campRecommendVo.getSize()-1)/ campRecommendVo.getSize());
        session.setAttribute("campRecommendVo", campRecommendVo);
        model.addAttribute("campRecommendVo", campRecommendVo);

        return "search/searchPage";
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
            CampRecommendVo campRecommendVo = (CampRecommendVo) session.getAttribute("campRecommendVo");
            if (campRecommendVo.getPage() != page) {
                campRecommendVo.setPage(page);
            } else if (!campRecommendVo.getSortBy().equals(sortBy)) {
                campRecommendVo.setSortBy(sortBy);
                campRecommendVo.setSortDirection(sortDirection);
                campRecommendVo.setPage(1);
                if (sortBy.equals("name")) {
                    campRecommendVo.setCampRecommendList(campRecommendVo.listSortBy(campRecommendVo.getCampRecommendList()));
                } else if (sortBy.equals("addr")) {
                    campRecommendVo.setCampRecommendList(campRecommendVo.listSortBy(campRecommendVo.getCampRecommendList()));
                } else if (sortBy.equals("score")) {
                    List<CampVo> list = campRecommendVo.getCampRecommendListNotVisited();
                    List<CampVo> reverseList = new ArrayList<>();
                    if (sortDirection.equals("DESC")) {
                        campRecommendVo.setCampRecommendList(list);
                    } else {
                        for (int i = 0 ; i < list.size() ; i++) {
                            reverseList.add(list.get(list.size()-i-1));
                        }
                        campRecommendVo.setCampRecommendList(reverseList);
                    }
                }
            } else if (!campRecommendVo.getSortDirection().equals(sortDirection)) {
                campRecommendVo.setSortBy(sortBy);
                campRecommendVo.setSortDirection(sortDirection);
                campRecommendVo.setPage(1);
                List<CampVo> list = campRecommendVo.getCampRecommendList();
                List<CampVo> reverseList = new ArrayList<>();
                for (int i = 0 ; i < list.size() ; i++) {
                    reverseList.add(list.get(list.size()-i-1));
                }
                campRecommendVo.setCampRecommendList(reverseList);
            }
            
            result.put("campRecommendList", campRecommendVo.getCampRecommendList());
            result.put("totalPages", campRecommendVo.getTotalPages());
            result.put("page", campRecommendVo.getPage());
            result.put("size", campRecommendVo.getSize());
            result.put("pageMaxDisplay", campRecommendVo.getPageMaxDisplay());
            result.put("result", "success");
        } else {
            result.put("result", "fail");
        }

        return result;
    }

    @PostMapping("/re_search")
    @ResponseBody
    public Map<String, Object> re_search(HttpSession session,
                                         @RequestParam(value="searchField") String searchField,
                                         @RequestParam(value="searchWord") String searchWord,
                                         @RequestParam(value="campType[]") List<String> campTypeList) {
        Map<String, Object> result = new HashMap<>();
        Member member = (Member) session.getAttribute("loginUser");
        if (member != null) {
            CampRecommendVo campRecommendVo = new CampRecommendVo();
            campRecommendVo.setPage(1);
            campRecommendVo.setSortBy(((CampRecommendVo)session.getAttribute("campRecommendVo")).getSortBy());
            campRecommendVo.setSortDirection(((CampRecommendVo)session.getAttribute("campRecommendVo")).getSortDirection());
            campRecommendVo.setSize(((CampRecommendVo)session.getAttribute("campRecommendVo")).getSize());
            campRecommendVo.setPageMaxDisplay(((CampRecommendVo)session.getAttribute("campRecommendVo")).getPageMaxDisplay());
            campRecommendVo.setUseyn(((CampRecommendVo)session.getAttribute("campRecommendVo")).getUseyn());
            campRecommendVo.setSearchField(searchField);
            campRecommendVo.setSearchWord(searchWord);

            String [] campType = campRecommendVo.getCampType();
            for (int i = 0 ; i < campRecommendVo.getCampTypeArray().length ; i++) {
                String type = campRecommendVo.getCampTypeArray()[i][0];
                if (campTypeList.contains(type)) {
                    campType[i] = type;
                } else {
                    campType[i] = "";
                }
            }
            campRecommendVo.setCampType(campType);

            campRecommendVo.setCampList(campService.getCampScanList(campRecommendVo));
            campService.saveCampRecommendList(campRecommendVo.getCampList(), member, campRecommendVo);
            campRecommendVo.setCampRecommendList(campRecommendVo.getCampRecommendListNotVisited());
            campRecommendVo.setTotalPages((campRecommendVo.getCampRecommendList().size()+ campRecommendVo.getSize()-1)/ campRecommendVo.getSize());

            session.setAttribute("campRecommendVo", campRecommendVo);

            result.put("campRecommendVo", campRecommendVo);
            result.put("result", "success");
        } else {
            result.put("result", "fail");
        }

        return result;
    }

    @PostMapping("/reloadMap")
    @ResponseBody
    public Map<String, Object> reloadMap(HttpSession session) {
        Map<String, Object> result = new HashMap<>();
        Member member = (Member) session.getAttribute("loginUser");
        if (member != null) {
            CampRecommendVo campRecommendVo = (CampRecommendVo) session.getAttribute("campRecommendVo");
            result.put("campRecommendVo", campRecommendVo);
            result.put("result", "success");
        } else {
            result.put("result", "fail");
        }

        return result;
    }


    @GetMapping("/api/search")
    @ResponseBody
    public ResponseEntity<List<Camp>> searchCamps(@RequestParam("keyword") String keyword) {

       List<Camp> camps = campService.searchCamps(keyword);
        return ResponseEntity.ok(camps);
    }

    @GetMapping("/api/validate")
    @ResponseBody
    public ResponseEntity<Map<String, Boolean>> validateCampName(@RequestParam("campName") String campName){
        boolean isValid = campService.isValidCampName(campName);
        Map<String, Boolean> response = new HashMap<>();
        response.put("valid", isValid);
        return ResponseEntity.ok(response);
    }



}
