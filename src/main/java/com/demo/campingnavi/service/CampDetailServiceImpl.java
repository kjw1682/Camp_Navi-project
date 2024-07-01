package com.demo.campingnavi.service;

import com.demo.campingnavi.domain.Recommend;
import com.demo.campingnavi.model.ApiImageResponse;
import com.demo.campingnavi.model.ApiResponse;
import com.demo.campingnavi.repository.jpa.RecommendRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Service
public class CampDetailServiceImpl implements CampDetailService {

    @Autowired
    RecommendRepository recommendRepo;

    private final WebClient webClient;

    private static final Logger logger = Logger.getLogger(CampDetailServiceImpl.class.getName());

    public CampDetailServiceImpl(final WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public List<ApiResponse.Item> DataFromApi(String mapX, String mapY) {

        String serviceKey = "YV8OhM3PmYs8nZRYBGLvcJ3c%2Bl3I13ySsOnUgSm4N1y5sae29L3T3cdo3E8hty%2FooLqQUATTLzf2jxnOpuP15w%3D%3D";
        String MobileOS = "ETC";
        String MobileApp = "AppTest";
        String radius = "5";
        String type = "json";

        String baseUrl = "https://apis.data.go.kr/B551011/GoCamping";
        String requestUrl = baseUrl + "/locationBasedList?" +
                "serviceKey=" + serviceKey +
                "&MobileOS=" + MobileOS +
                "&MobileApp=" + MobileApp +
                "&mapX=" + mapX +
                "&mapY=" + mapY +
                "&radius=" + radius +
                "&_type=" + type;
        logger.info("API 호출 URL: " + requestUrl);

        try {
            ApiResponse apiResponse = webClient.get()
                    .uri(requestUrl)
                    .retrieve()
                    .bodyToMono(ApiResponse.class)
                    .block();

            logger.info("API Response: " + apiResponse);

            if (apiResponse != null && apiResponse.getResponse().getBody() != null) {
                return apiResponse.getResponse().getBody().getItems().getItemList();
            } else {
                logger.warning("Response body or body content is null");
                return new ArrayList<>();
            }
        } catch (Exception e) {
            logger.severe("Exception occurred while calling API: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public List<ApiImageResponse.Item> DataFromApiImage(String contentId) {
        String serviceKey = "YV8OhM3PmYs8nZRYBGLvcJ3c%2Bl3I13ySsOnUgSm4N1y5sae29L3T3cdo3E8hty%2FooLqQUATTLzf2jxnOpuP15w%3D%3D";
        String MobileOS = "ETC";
        String MobileApp = "AppTest";
        String type = "json";

        String baseUrl = "https://apis.data.go.kr/B551011/GoCamping";
        String requestUrl = baseUrl + "/imageList?" +
                "serviceKey=" + serviceKey +
                "&MobileOS=" + MobileOS +
                "&MobileApp=" + MobileApp +
                "&contentId=" + contentId +
                "&_type=" + type;

        logger.info("API 호출 URL: " + requestUrl);

        try {
            ApiImageResponse apiImageResponse = webClient.get()
                    .uri(requestUrl)
                    .retrieve()
                    .bodyToMono(ApiImageResponse.class)
                    .block();

            logger.info("API Response: " + apiImageResponse);

            if (apiImageResponse != null && apiImageResponse.getResponse().getBody() != null) {
                return apiImageResponse.getResponse().getBody().getItems().getItemList();
            } else {
                logger.warning("Response body or body content is null");
                return new ArrayList<>();
            }
        } catch (Exception e) {
            logger.severe("Exception occurred while calling API: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public void addToJjimlist(Recommend recommend) {
        recommendRepo.save(recommend);
    }

    @Transactional
    @Override
    public void removeFromJjimlist(int mseq, int cseq) {
        recommendRepo.deleteByMemberAndCamp(mseq, cseq);
    }

    @Override
    public boolean isCampJjimmedByUser(int mseq, int cseq) {
        return recommendRepo.existsByMember_MseqAndCamp_Cseq(mseq, cseq);
    }
}
