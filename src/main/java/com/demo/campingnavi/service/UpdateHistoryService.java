package com.demo.campingnavi.service;

import com.demo.campingnavi.domain.UpdateHistory;

import java.util.List;

public interface UpdateHistoryService {
    void saveUpdateHistory(UpdateHistory updateHistory);
    List<UpdateHistory> getUpdateHistoryList(String kind, String result);
    List<UpdateHistory> getUpdateHistoryList(String kind);
}
