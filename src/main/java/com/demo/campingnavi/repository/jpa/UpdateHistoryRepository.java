package com.demo.campingnavi.repository.jpa;

import com.demo.campingnavi.domain.UpdateHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UpdateHistoryRepository extends JpaRepository<UpdateHistory, Integer> {
    List<UpdateHistory> findByKindOrderByUpdateTimeDesc(String update);

    @Query("SELECT updateHistory FROM UpdateHistory updateHistory " +
            "WHERE updateHistory.kind LIKE %:kind% " +
            "AND updateHistory.result LIKE %:result% " +
            "ORDER BY updateHistory.useq ")
    List<UpdateHistory> getUpdateHistoryList(String kind, String result);
}
