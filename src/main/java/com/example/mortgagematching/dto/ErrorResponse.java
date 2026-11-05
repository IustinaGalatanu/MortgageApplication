package com.example.mortgagematching.dto;

import java.util.Map;

/**
 * Error body returned for failed requests.
 *
 * @param message     human readable summary of what went wrong
 * @param fieldErrors validation messages keyed by field name; empty when not applicable
 */
public record ErrorResponse(String message, Map<String, String> fieldErrors) {

    public static ErrorResponse of(String message) {
        return new ErrorResponse(message, Map.of());
    }
}

