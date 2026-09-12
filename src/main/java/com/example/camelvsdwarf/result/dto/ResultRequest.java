package com.example.camelvsdwarf.result.dto;

import com.example.camelvsdwarf.result.ResultStatus;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record ResultRequest(
        @NotNull Long registrationId,
        @Min(1) Integer startingPosition,
        @Min(1) Integer finalPosition,
        @DecimalMin(value = "0.001") BigDecimal completionTimeSeconds,
        @DecimalMin(value = "0.0") BigDecimal penaltyTimeSeconds,
        @NotNull ResultStatus status,
        @Size(max = 500) String notes
) {
}