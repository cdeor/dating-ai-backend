package com.github.cdeor.dating_ai_backend.utils;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public record ValidationResult(
        boolean valid,
        ResponseStatusException exception
) {
    public static ValidationResult success() {
        return new ValidationResult(true, null);
    }

    public static ValidationResult failure(String msg) {
        return new ValidationResult(false,
                new ResponseStatusException(HttpStatus.NOT_FOUND,
                        msg));
    }
}
