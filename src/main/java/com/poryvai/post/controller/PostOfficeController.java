package com.poryvai.post.controller;

import com.poryvai.post.dto.CreatePostOfficeRequest;
import com.poryvai.post.dto.PostOfficeSearchParams;
import com.poryvai.post.model.PostOffice;
import com.poryvai.post.service.PostOfficeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing PostOffice entities.
 * Provides API endpoints for basic CRUD operations on post offices.
 */
@RestController
@RequestMapping("/api/v1/post-offices")
@RequiredArgsConstructor
@Slf4j
public class PostOfficeController {

    private final PostOfficeService postOfficeService;

    /**
     * Creates a new post office.
     *
     * @param request The {@link CreatePostOfficeRequest} containing post office details.
     * @return The created {@link PostOffice} object.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PostOffice create(@Valid @RequestBody CreatePostOfficeRequest request) {
        log.info("Received request to create post office: {}", request.getName());
        return postOfficeService.create(request);
    }

    /**
     * Retrieves a post office by its ID.
     *
     * @param id The ID of the post office to retrieve.
     * @return The {@link PostOffice} object. HTTP status 200 OK.
     */
    @GetMapping("/{id}")
    public PostOffice getById(@PathVariable Long id) {
        log.info("Received request to get post office by ID: {}", id);
        return postOfficeService.getById(id);
    }

    /**
     * Retrieves a paginated list of post offices, allowing for dynamic filtering based on various criteria.
     *
     * @param params   {@link PostOfficeSearchParams} containing optional filters such as name, city, postcode, and street.
     * These parameters are typically passed as request parameters (query string).
     * @param pageable {@link Pageable} object for pagination and sorting (e.g., page=0&size=10&sort=id,asc).
     * Automatically resolved by Spring from request parameters.
     * @return A {@link Page} of {@link PostOffice} objects matching the search criteria.
     */
    @GetMapping
    public Page<PostOffice> findAll(PostOfficeSearchParams params, Pageable pageable) {
        log.info("Received request to find all post offices with params: {} and pageable: {}", params, pageable);
        return postOfficeService.findAll(params, pageable);
    }

    /**
     * Updates an existing post office.
     *
     * @param id The ID of the post office to update.
     * @param request The {@link CreatePostOfficeRequest} containing updated details.
     * @return The updated {@link PostOffice} object. HTTP status 200 OK.
     */
    @PutMapping("/{id}")
    public PostOffice update(@PathVariable Long id, @Valid @RequestBody CreatePostOfficeRequest request) {
        log.info("Received request to update post office with ID {}: {}", id, request.getName());
        return postOfficeService.update(id, request);
    }

    /**
     * Deletes a post office by its ID.
     *
     * @param id The ID of the post office to delete.
     * @return No content. HTTP status 204 (No Content).
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        log.info("Received request to delete post office by ID: {}", id);
        postOfficeService.delete(id);
    }


}
