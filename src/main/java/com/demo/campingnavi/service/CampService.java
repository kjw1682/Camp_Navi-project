package com.demo.campingnavi.service;

import com.demo.campingnavi.domain.Camp;
import com.demo.campingnavi.domain.Member;
import com.demo.campingnavi.dto.CampRecommendVo;
import com.demo.campingnavi.dto.CampVo;
import com.demo.campingnavi.repository.jpa.CampRepository;

import java.util.List;

public interface CampService {
    Camp getCampByCseq(int cseq);
    CampVo getCampVoByCseq(int cseq, Member member);
    List<Camp> getCampScanList(CampRecommendVo campRecommendVo);
    void saveCampRecommendList(List<Camp> filteredList, Member member, CampRecommendVo campRecommendVo);
    List<Camp> searchItems(String keyword);
    void campAllDisabled();
    List<Camp> getCampListByUseyn(String useyn);

    List<Camp> searchCamps(String keyword);
    Camp getCampByName(String campName);

    boolean isValidCampName(String campName);
}
