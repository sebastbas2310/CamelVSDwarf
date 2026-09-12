package com.example.camelvsdwarf.user.dto;

import java.time.LocalDateTime;

public record UserResponse(Long id, String fullName, String email, Role role, boolean enabled, LocalDateTime createdAt) {
}