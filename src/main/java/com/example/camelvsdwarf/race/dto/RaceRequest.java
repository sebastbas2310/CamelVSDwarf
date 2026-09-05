package com.example.camelvsdwarf.race;

import com.example.camelvsdwarf.race.RaceType;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public record RaceRequest(
        @NotBlank @Size(max = 150) String name,
        @Size(max = 600) String description,
        @NotNull @Future LocalDateTime scheduledAt,
        @NotBlank @Size(max = 150) String startLocation,
        @NotBlank @Size(max = 150) String finishLocation,
        @NotNull @Min(1) Integer distanceMeters,
        @NotNull @Min(2) Integer maximumParticipants,
        @NotNull RaceType type,
        @NotNull @Future LocalDateTime registrationDeadline
) {
}