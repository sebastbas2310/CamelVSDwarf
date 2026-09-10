package com.example.camelvsdwarf.user;

import jakarta.validation.constraints.NotNull;

public record UserStatusRequest(@NotNull UserStatus status) {
}
