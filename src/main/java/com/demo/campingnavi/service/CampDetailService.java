package com.demo.campingnavi.service;

import com.demo.campingnavi.domain.Recommend;
import com.demo.campingnavi.model.ApiImageResponse;
import com.demo.campingnavi.model.ApiResponse;

import java.util.List;

public interface CampDetailService {
    //캠핑장 정보 불러오기
    List<ApiResponse.Item> DataFromApi(String mapX, String mapY);
    //캠핑장 이미지 정보 불러오기
    List<ApiImageResponse.Item> DataFromApiImage(String contendId);
    //찜하기 저장
    void addToJjimlist(Recommend recommend);
    //찜하기 삭제
    void removeFromJjimlist(int mseq, int cseq);
    //찜하기 정보 유무 확인
    boolean isCampJjimmedByUser(int mseq, int cseq);

}
