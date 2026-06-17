package com.ai.resumeanalyzer.repository;

import com.ai.resumeanalyzer.entity.ResumeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResumeRepository
        extends JpaRepository<ResumeEntity, Long> {
}