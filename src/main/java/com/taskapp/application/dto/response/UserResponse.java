package com.taskapp.application.dto.response;

import com.taskapp.domain.entity.User;

import java.util.UUID;

public record UserResponse(
    UUID id,
    String name,
    String email
) {
    public static UserResponse from(User user) {
        if (user == null) return null;
        return new UserResponse(user.getId(), user.getName(), user.getEmail());
    }
}
