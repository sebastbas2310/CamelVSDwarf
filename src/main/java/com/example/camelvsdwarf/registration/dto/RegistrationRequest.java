package com.example.camelvsdwarf.registration.dto;

import com.example.camelvsdwarf.registration.ParticipantType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record RegistrationRequest(
        @NotNull ParticipantType participantType,
        Long competitorId,
        Long teamId,
        @NotNull @Min(1) Integer startingPosition
) {
}