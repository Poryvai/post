package com.poryvai.post.service;

import com.poryvai.post.dto.CreatePostOfficeRequest;
import com.poryvai.post.model.PostOffice;

import java.util.List;

/**
 * Service interface for managing {@link PostOffice} entities.
 * Defines the contract for basic CRUD operations.
 */
public interface PostOfficeService {

    /**
     * Creates a new post office based on the provided request.
     *
     * @param request The {@link CreatePostOfficeRequest} containing details for the new post office.
     * @return The newly created and persisted {@link PostOffice} object.
     */
    PostOffice create(CreatePostOfficeRequest request);

    /**
     * Retrieves a post office by its unique ID.
     *
     * @param id The unique ID of the post office.
     * @return The {@link PostOffice} object corresponding to the given ID.
     * @throws com.poryvai.post.exception.NotFoundException if no post office with the specified ID is found.
     */
    PostOffice getById(Long id);

    /**
     * Retrieves all post offices.
     *
     * @return A list of all {@link PostOffice} objects.
     */
    List<PostOffice> getAll();

    /**
     * Updates an existing post office identified by its ID.
     *
     * @param id      The unique ID of the post office to be updated.
     * @param request The {@link CreatePostOfficeRequest}
     * containing the updated details.
     * @return The updated {@link PostOffice} object.
     * @throws com.poryvai.post.exception.NotFoundException if no post office with the specified ID is found.
     */
    PostOffice update(Long id, CreatePostOfficeRequest request);

    /**
     * Deletes a post office by its unique ID.
     *
     * @param id The unique ID of the post office to be deleted.
     * @throws com.poryvai.post.exception.NotFoundException if no post office with the specified ID is found.
     */
    void delete(Long id);
}
