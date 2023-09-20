package com.github.wesleybritovlk.leasingserviceapi.leasingcart;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static java.lang.System.currentTimeMillis;
import static java.util.Objects.requireNonNull;
import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/carts")
@Tag(name = "Leasing Carts Endpoint", description = "RESTful Endpoint for managing carts.")
@RequiredArgsConstructor
class LeasingCartController {
    private final LeasingCartService service;
    private final LeasingCartMapper mapper;

    private static final Logger LOGGER = getLogger(LeasingCartController.class);

    @PostMapping
    @Operation(summary = "Create a new cart", description = "Create a new cart and return the created cart's data")
    ResponseEntity<LeasingCartResponse> create(@Valid @RequestBody LeasingCartRequest requestCreate) {
        var startTime = currentTimeMillis();
        var leasingCart = service.create(requestCreate);
        var response = mapper.toResponse(leasingCart);
        var post = ResponseEntity.status(CREATED).body(response);
        LOGGER.info("M POST /carts {} , Created leasing_cart ID: {} in {}ms", post.getStatusCode(), requireNonNull(post.getBody()).id(), currentTimeMillis() - startTime);
        return post;
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a cart by ID", description = "Retrieve a specific cart based on its ID")
    ResponseEntity<LeasingCartResponse> getById(@PathVariable UUID id) {
        var startTime = currentTimeMillis();
        var response = service.findById(id);
        var getById = ResponseEntity.ok(response);
        LOGGER.info("M GET /carts/ID {} : Returned leasing_cart ID: {} in {}ms", getById.getStatusCode(), requireNonNull(getById.getBody()).id(), currentTimeMillis() - startTime);
        return getById;
    }

    @GetMapping
    @Operation(summary = "Get all carts", description = "Retrieve a page of all registered carts")
    ResponseEntity<Page<LeasingCartResponse>> getAll(@PageableDefault(size = 5, sort = "updatedAt", direction = Sort.Direction.DESC) Pageable pageable) {
        var startTime = currentTimeMillis();
        var responses = service.findAll(pageable);
        var getAll = ResponseEntity.ok(responses);
        LOGGER.info("M GET /carts {} : Returned {} leasing_carts in {}ms", getAll.getStatusCode(), requireNonNull(getAll.getBody()).getTotalElements(), currentTimeMillis() - startTime);
        return getAll;
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a cart", description = "Update the data of an existing cart based on its ID")
    ResponseEntity<LeasingCartResponse> update(@PathVariable UUID id,
                                               @Valid @RequestBody LeasingCartRequest requestUpdate) {
        var startTime = currentTimeMillis();
        var leasingCart = service.update(id, requestUpdate);
        var response = mapper.toResponse(leasingCart);
        var put = ResponseEntity.ok(response);
        LOGGER.info("M PUT /carts/ID {} : Updated leasing_cart ID: {} in {}ms", put.getStatusCode(), requireNonNull(put.getBody()).id(), currentTimeMillis() - startTime);
        return put;
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a cart", description = "Delete an existing cart based on its ID")
    ResponseEntity<String> delete(@PathVariable UUID id) {
        var startTime = currentTimeMillis();
        service.delete(id);
        var delete = ResponseEntity.ok("Leasing-cart deleted successfully");
        LOGGER.info("M DELETE /carts/ID {} : Deleted leasing_cart ID: {} in {}ms", delete.getStatusCode(), id, currentTimeMillis() - startTime);
        return delete;
    }
}