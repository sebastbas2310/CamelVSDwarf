package com.example.camelvsdwarf.user;

public record TokenResponse(String accessToken, String tokenType, long expiresIn, UserResponse user) {
}