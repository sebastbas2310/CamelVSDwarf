package com.example.camelvsdwarf.race;

import com.example.camelvsdwarf.race.RaceStatus;
import com.example.camelvsdwarf.race.RaceType;

import java.time.LocalDateTime;

public record RaceResponse(
        Long id, String name, String description, LocalDateTime scheduledAt,
        String startLocation, String finishLocation, Integer distanceMeters,
        Integer maximumParticipants, RaceType type, RaceStatus status,
        Long organizerId, LocalDateTime registrationDeadline,
        LocalDateTime createdAt, LocalDateTime updatedAt
) {
}