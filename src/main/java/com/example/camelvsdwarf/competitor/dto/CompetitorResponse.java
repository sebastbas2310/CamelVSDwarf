package com.example.camelvsdwarf.competitor;

import com.example.camelvsdwarf.competitor.CompetitorStatus;
import com.example.camelvsdwarf.competitor.CompetitorType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record CompetitorResponse(
        Long id, String name, String nickname, CompetitorType type, LocalDate dateOfBirth,
        BigDecimal weight, BigDecimal height, String origin, CompetitorStatus status,
        LocalDateTime registeredAt, int victories, int defeats, int completedRaces
) {
}