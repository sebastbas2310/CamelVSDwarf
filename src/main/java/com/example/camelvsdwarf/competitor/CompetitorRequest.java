package com.example.camelvsdwarf.competitor;

import com.example.camelvsdwarf.competitor.CompetitorType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CompetitorRequest(
        @NotBlank @Size(max = 120) String name,
        @NotBlank @Size(max = 80) String nickname,
        @NotNull CompetitorType type,
        LocalDate dateOfBirth,
        @NotNull @DecimalMin(value = "0.01") BigDecimal weight,
        @NotNull @DecimalMin(value = "0.01") BigDecimal height,
        @Size(max = 120) String origin
) {
}