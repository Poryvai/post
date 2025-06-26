package com.poryvai.post.util;

import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * A utility component for generating unique identifiers.
 * This class provides a method for creating universally unique identifiers (UUIDs)
 * in their string representation.
 */
@Component
public class CommonGenerator {

    /**
     * Generates a new unique {@link UUID} and returns its {@link String} representation.
     * This is commonly used for creating unique tracking numbers or other string-based identifiers.
     *
     * @return A newly generated unique identifier as a String.
     */
    public String uuid() {
        return UUID.randomUUID().toString();
    }
}
