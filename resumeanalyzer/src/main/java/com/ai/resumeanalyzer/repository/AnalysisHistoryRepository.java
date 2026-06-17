package com.ai.resumeanalyzer.repository;

import com.ai.resumeanalyzer.entity.AnalysisHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnalysisHistoryRepository
        extends JpaRepository<AnalysisHistory, Long> {
    AnalysisHistory findTopByOrderByIdDesc();
}