package com.poryvai.post.controller;

import com.poryvai.post.exception.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Global exception handler for REST controllers, utilizing Spring's {@link RestControllerAdvice}.
 * This class provides centralized exception handling for the application's REST API,
 * ensuring consistent error responses. It specifically handles {@link NotFoundException}
 * by mapping it to an HTTP 404 Not Found status and returning a simplified error DTO.
 */
@RestControllerAdvice
public class RestExceptionHandler {

    /**
     * Handles {@link NotFoundException} by mapping it to an HTTP 404 Not Found status.
     * When a {@link NotFoundException} is thrown, this method returns an {@link ErrorDto}
     * containing the exception's message, along with an HTTP 404 status code.
     *
     * @param e The {@link NotFoundException} that was thrown.
     * @return An {@link ErrorDto} containing the error message.
     */
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public ErrorDto notFound(NotFoundException e) {
        return new ErrorDto(e.getMessage());
    }

    /**
     * A simple record (Java 16+) representing the structure of an error response.
     * It typically contains a single field for the error message.
     *
     * @param message The descriptive error message.
     */
    public record ErrorDto(String message) {
    }
}
