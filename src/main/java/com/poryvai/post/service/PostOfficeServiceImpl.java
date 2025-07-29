package com.poryvai.post.service;

import com.poryvai.post.dto.CreatePostOfficeRequest;
import com.poryvai.post.dto.PostOfficeSearchParams;
import com.poryvai.post.exception.NotFoundException;
import com.poryvai.post.model.PostOffice;
import com.poryvai.post.repository.PostOfficeRepository;
import com.poryvai.post.util.PostOfficeSpecifications;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service implementation for managing {@link PostOffice} entities.
 * Provides concrete implementations for basic CRUD operations.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class PostOfficeServiceImpl implements PostOfficeService{

    private final PostOfficeRepository postOfficeRepository;

    /**
     * Creates a new post office based on the provided request.
     *
     * @param request The {@link CreatePostOfficeRequest} containing details for the new post office.
     * @return The newly created and persisted {@link PostOffice} object.
     */
    @Override
    @Transactional
    public PostOffice create(CreatePostOfficeRequest request) {
        log.info("Creating new post office: {}", request.getName());
        PostOffice postOffice = PostOffice.builder()
                .name(request.getName())
                .city(request.getCity())
                .postcode(request.getPostcode())
                .street(request.getStreet())
                .build();

        PostOffice savedPostOffice = postOfficeRepository.save(postOffice);
        log.info("Post office created successfully with ID: {}", savedPostOffice.getId());
        return savedPostOffice;
    }

    /**
     * Retrieves a post office by its unique ID.
     *
     * @param id The unique ID of the post office.
     * @return The {@link PostOffice} object corresponding to the given ID.
     * @throws NotFoundException if no post office with the specified ID is found.
     */
    @Override
    @Transactional(readOnly = true)
    public PostOffice getById(Long id) {
        log.info("Fetching post office by ID: {}", id);
        return postOfficeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Post Office with ID " + id + " not found"));
    }

    /**
     * Retrieves all post offices.
     *
     * @return A list of all {@link PostOffice} objects.
     */
    @Override
    @Transactional(readOnly = true)
    public List<PostOffice> getAll() {
        log.info("Fetching all post offices");
        return postOfficeRepository.findAll();
    }

    /**
     * Updates an existing post office identified by its ID.
     *
     * @param id      The unique ID of the post office to be updated.
     * @param request The {@link CreatePostOfficeRequest}
     *                containing the updated details.
     * @return The updated {@link PostOffice} object.
     * @throws NotFoundException if no post office with the specified ID is found.
     */
    @Override
    @Transactional
    public PostOffice update(Long id, CreatePostOfficeRequest request) {
        log.info("Updating post office with ID {}: {}", id, request.getName());
        return postOfficeRepository.findById(id)
                .map(existingPostOffice -> {
                    existingPostOffice.setName(request.getName());
                    existingPostOffice.setCity(request.getCity());
                    existingPostOffice.setPostcode(request.getPostcode());
                    existingPostOffice.setStreet(request.getStreet());
                    return postOfficeRepository.save(existingPostOffice);
                })
                .orElseThrow(() -> new NotFoundException("Post Office with ID " + id + " not found"));
    }

    /**
     * Deletes a post office by its unique ID.
     *
     * @param id The unique ID of the post office to be deleted.
     * @throws NotFoundException if no post office with the specified ID is found.
     */
    @Override
    @Transactional
    public void delete(Long id) {
        log.info("Deleting post office by ID: {}", id);
        if (!postOfficeRepository.existsById(id)) {
            throw new NotFoundException("Post Office with ID " + id + " not found");
        }
        postOfficeRepository.deleteById(id);
    }

    /**
     * Retrieves a paginated list of PostOffice entities based on dynamic search parameters.
     *
     * @param params   {@link PostOfficeSearchParams} containing optional filters for post offices.
     * @param pageable {@link Pageable} object for pagination and sorting.
     * @return A {@link Page} of {@link PostOffice} objects matching the search criteria.
     */
    @Override
    public Page<PostOffice> findAll(PostOfficeSearchParams params, Pageable pageable) {
        log.info("Retrieving post offices with search parameters: {} and pageable: {}", params, pageable);
        return postOfficeRepository.findAll(PostOfficeSpecifications.bySearchParams(params), pageable);
    }
}
