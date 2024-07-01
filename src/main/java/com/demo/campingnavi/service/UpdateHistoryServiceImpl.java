package com.demo.campingnavi.service;

import com.demo.campingnavi.domain.UpdateHistory;
import com.demo.campingnavi.repository.jpa.UpdateHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UpdateHistoryServiceImpl implements UpdateHistoryService {

    @Autowired
    private UpdateHistoryRepository updateHistoryRepository;

    @Override
    public List<UpdateHistory> getUpdateHistoryList(String kind) {
        return updateHistoryRepository.findByKindOrderByUpdateTimeDesc(kind);
    }

    @Override
    public List<UpdateHistory> getUpdateHistoryList(String kind, String result) {
        return updateHistoryRepository.getUpdateHistoryList(kind, result);
    }

    @Override
    public void saveUpdateHistory(UpdateHistory updateHistory) {
        updateHistoryRepository.save(updateHistory);
    }
}
