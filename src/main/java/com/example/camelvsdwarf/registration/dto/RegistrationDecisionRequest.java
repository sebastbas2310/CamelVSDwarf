package com.example.camelvsdwarf.registration.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegistrationDecisionRequest(@NotBlank @Size(max = 500) String reason) {
}