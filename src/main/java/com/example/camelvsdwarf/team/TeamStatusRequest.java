package com.example.camelvsdwarf.team;

import jakarta.validation.constraints.NotNull;

public record TeamStatusRequest(@NotNull TeamStatus status) {
}
