package com.example.camelvsdwarf.result.dto;

import com.example.camelvsdwarf.result.ResultStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ResultResponse(
        Long id, Long raceId, Long registrationId, Integer startingPosition, Integer finalPosition,
        BigDecimal completionTimeSeconds, BigDecimal penaltyTimeSeconds, ResultStatus status,
        String notes, Long recordedById, LocalDateTime recordedAt
) {
}