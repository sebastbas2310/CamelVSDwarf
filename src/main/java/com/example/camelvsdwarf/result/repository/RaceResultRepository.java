package com.example.camelvsdwarf.result.repository;

import com.example.camelvsdwarf.result.dto.ResultStatus;
import com.example.camelvsdwarf.result.entity.RaceResult;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RaceResultRepository extends JpaRepository<RaceResult, Long> {
    List<RaceResult> findByRaceIdOrderByFinalPositionAsc(Long raceId);
    boolean existsByRaceIdAndFinalPosition(Long raceId, Integer finalPosition);
    boolean existsByRaceIdAndFinalPositionAndStatus(Long raceId, Integer finalPosition, ResultStatus status);
    boolean existsByRaceIdAndRegistrationId(Long raceId, Long registrationId);
}