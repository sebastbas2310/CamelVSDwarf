package com.example.camelvsdwarf.team;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface teamRepository extends JpaRepository<Team, Long> {
    boolean existsByNameIgnoreCase(String name);
    Page<Team> findByNameContainingIgnoreCase(String name, Pageable pageable);
    Page<Team> findByStatus(teamStatus status, Pageable pageable);
}