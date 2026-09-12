package com.example.camelvsdwarf.competitor.repository;

import com.example.camelvsdwarf.competitor.Competitor;
import com.example.camelvsdwarf.competitor.CompetitorStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompetitorRepository extends JpaRepository<Competitor, Long> {
    boolean existsByNicknameIgnoreCase(String nickname);
    Page<Competitor> findByNameContainingIgnoreCaseOrNicknameContainingIgnoreCase(String name, String nickname, Pageable pageable);
    Page<Competitor> findByStatus(CompetitorStatus status, Pageable pageable);
}