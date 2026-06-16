package com.taskapp.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record TagRequest(
    @NotBlank(message = "Tag name is required")
    @Size(max = 30, message = "Name must be at most 30 characters")
    String name,

    @Pattern(regexp = "^#[0-9A-Fa-f]{6}$", message = "Color must be a valid hex color (e.g. #6366f1)")
    String color
) {}
