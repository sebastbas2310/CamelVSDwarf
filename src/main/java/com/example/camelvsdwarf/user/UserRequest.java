package com.example.camelvsdwarf.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UserRequest(@NotBlank @Size(max = 120) String fullName, @NotBlank @Email String email, @Size(min = 8) String password, @NotNull Role role) {
}
