package com.example.camelvsdwarf.race.dto;

import com.example.camelvsdwarf.race.RaceStatus;
import jakarta.validation.constraints.NotNull;

public record RaceStatusRequest(@NotNull RaceStatus status) {
}