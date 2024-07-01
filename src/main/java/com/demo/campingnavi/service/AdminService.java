package com.demo.campingnavi.service;

import java.util.Map;

public interface AdminService {
    String recommendModelUpdate();
    String campDataUpdate();
    int getCampingTotalCount();
    String getCampingDataFromApi(int page);
    String getCampingDataIntegration(int totalPage);
    Map<String, Integer> getCrawlingStatus();
    String getCrawlingData();
    String getCrawlingDataIntegration();
    void clearRatingTempFile(int start_number);
}
