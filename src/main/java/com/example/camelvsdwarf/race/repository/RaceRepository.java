package com.example.camelvsdwarf.race.repository;

import com.example.camelvsdwarf.race.Race;
import com.example.camelvsdwarf.race.RaceStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface RaceRepository extends JpaRepository<Race, Long> {
    Page<Race> findByStatus(RaceStatus status, Pageable pageable);
    Page<Race> findByNameContainingIgnoreCase(String name, Pageable pageable);
    Page<Race> findByScheduledAtAfter(LocalDateTime dateTime, Pageable pageable);
}