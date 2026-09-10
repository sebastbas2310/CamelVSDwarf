package com.example.camelvsdwarf.result;

import jakarta.validation.constraints.NotNull;

public record ResultStatusRequest(@NotNull ResultStatus status) {
}
