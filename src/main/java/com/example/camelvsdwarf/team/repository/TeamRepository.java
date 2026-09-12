package com.example.camelvsdwarf.team.repository;

import com.example.camelvsdwarf.team.Team;
import com.example.camelvsdwarf.team.TeamStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepository extends JpaRepository<Team, Long> {
    boolean existsByNameIgnoreCase(String name);
    Page<Team> findByNameContainingIgnoreCase(String name, Pageable pageable);
    Page<Team> findByStatus(TeamStatus status, Pageable pageable);
}