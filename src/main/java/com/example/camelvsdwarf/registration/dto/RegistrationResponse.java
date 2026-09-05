package com.example.camelvsdwarf.registration;

import com.example.camelvsdwarf.registration.ParticipantType;
import com.example.camelvsdwarf.registration.RegistrationStatus;

import java.time.LocalDateTime;

public record RegistrationResponse(
        Long id, Long raceId, ParticipantType participantType, Long competitorId, Long teamId,
        LocalDateTime registeredAt, RegistrationStatus status, Integer startingPosition,
        String validationNotes, Long registeredById
) {
}