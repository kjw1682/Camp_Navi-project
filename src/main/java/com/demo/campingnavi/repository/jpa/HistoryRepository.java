package com.demo.campingnavi.repository.jpa;

import com.demo.campingnavi.domain.History;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HistoryRepository extends JpaRepository<History, Integer> {
}