package com.aiticketmanager.dto;

public record AuthResponse(
        String token,
        String role,
        String message
) {}
