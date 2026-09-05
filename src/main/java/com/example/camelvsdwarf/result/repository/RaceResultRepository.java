package com.example.camelvsdwarf.result;

import com.example.camelvsdwarf.result.RaceResult;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RaceResultRepository extends JpaRepository<RaceResult, Long> {
    List<RaceResult> findByRaceIdOrderByFinalPositionAsc(Long raceId);
    boolean existsByRaceIdAndFinalPosition(Long raceId, Integer finalPosition);
    boolean existsByRaceIdAndFinalPositionAndStatus(Long raceId, Integer finalPosition, ResultStatus status);
    boolean existsByRaceIdAndRegistrationId(Long raceId, Long registrationId);
}