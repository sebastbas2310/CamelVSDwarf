package com.example.camelvsdwarf.competitor;

import com.example.camelvsdwarf.competitor.CompetitorStatus;
import jakarta.validation.constraints.NotNull;

public record CompetitorStatusRequest(@NotNull CompetitorStatus status) {
}