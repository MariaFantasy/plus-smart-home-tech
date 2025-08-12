package ru.yandex.practicum.exception;

import org.springframework.http.HttpStatus;

@lombok.Data
@lombok.AllArgsConstructor
public class ErrorResponse {
    private final Throwable cause;
    private final StackTraceElement[] stackTrace;
    private final HttpStatus httpStatus;
    private final String userMessage;
    private final String message;
    private final Throwable[] suppressed;
    private final String localizedMessage;
}
