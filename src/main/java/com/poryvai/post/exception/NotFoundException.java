package com.poryvai.post.exception;

/**
 * Custom exception indicating that a requested resource was not found.
 * This exception typically maps to an HTTP 404 Not Found status,
 * making it suitable for Spring Web applications.
 */
public class NotFoundException extends RuntimeException {

    /**
     * Constructs a new NotFoundException with no detail message.
     * This constructor is used when the specific reason for not finding the resource
     * is not immediately available or needs to be generic.
     */
    public NotFoundException() {
    }

    /**
     * Constructs a new NotFoundException with the specified detail message.
     * This allows for providing specific information about which resource was not found.
     *
     * @param message The detail message (which is saved for later retrieval by the getMessage() method).
     */
    public NotFoundException(String message) {
        super(message);
    }
}
