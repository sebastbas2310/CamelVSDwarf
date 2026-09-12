package com.example.camelvsdwarf.registration.repository;

import com.example.camelvsdwarf.registration.RaceRegistration;
import com.example.camelvsdwarf.registration.dto.RegistrationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RaceRegistrationRepository extends JpaRepository<RaceRegistration, Long> {
    List<RaceRegistration> findByRaceId(Long raceId);
    boolean existsByRaceIdAndCompetitorId(Long raceId, Long competitorId);
    boolean existsByRaceIdAndTeamId(Long raceId, Long teamId);
    boolean existsByRaceIdAndStartingPosition(Long raceId, Integer startingPosition);
    long countByRaceIdAndStatus(Long raceId, RegistrationStatus status);
}