package com.example.camelvsdwarf.race;

import com.example.camelvsdwarf.race.RaceStatus;
import jakarta.validation.constraints.NotNull;

public record RaceStatusRequest(@NotNull RaceStatus status) {
}