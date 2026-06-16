package com.taskapp.application.dto.response;

import java.util.UUID;

public record AuthResponse(
    String token,
    String type,
    UUID userId,
    String name,
    String email
) {
    public AuthResponse(String token, UUID userId, String name, String email) {
        this(token, "Bearer", userId, name, email);
    }
}
