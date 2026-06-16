package com.taskapp.presentation.handler;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiError(
    int status,
    String error,
    String message,
    String path,
    LocalDateTime timestamp,
    List<FieldError> errors
) {
    public ApiError(int status, String error, String message, String path) {
        this(status, error, message, path, LocalDateTime.now(), null);
    }

    public ApiError(int status, String error, String message, String path, List<FieldError> fieldErrors) {
        this(status, error, message, path, LocalDateTime.now(), fieldErrors);
    }

    public record FieldError(String field, String message) {}
}
