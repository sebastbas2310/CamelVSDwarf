package com.example.camelvsdwarf.user;

import com.example.camelvsdwarf.user.dto.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UserRequest(@NotBlank @Size(max = 120) String fullName, @NotBlank @Email String email,@NotNull Role role) {
}
