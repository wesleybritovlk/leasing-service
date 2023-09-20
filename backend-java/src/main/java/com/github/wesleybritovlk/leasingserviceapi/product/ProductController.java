package com.github.wesleybritovlk.leasingserviceapi.product;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static java.lang.System.currentTimeMillis;
import static java.util.Objects.requireNonNull;
import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/products")
@Tag(name = "Products Endpoint", description = "RESTful Endpoint for managing products.")
@RequiredArgsConstructor
class ProductController {
    private final ProductService service;
    private final ProductMapper mapper;

    private static final Logger LOGGER = getLogger(ProductController.class);

    @PostMapping
    @Operation(summary = "Create a new product", description = "Create a new product and return the created product's data")
    ResponseEntity<ProductResponse> create(@Valid @RequestBody ProductRequest creationRequest) {
        var startTime = currentTimeMillis();
        var product = service.create(creationRequest);
        var response = mapper.toResponse(product);
        var post = ResponseEntity.status(CREATED).body(response);
        LOGGER.info("M POST /products {} , Created product ID: {} in {}ms", post.getStatusCode(), requireNonNull(post.getBody()).id(), currentTimeMillis() - startTime);
        return post;
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a product by ID", description = "Retrieve a specific product based on its ID")
    ResponseEntity<ProductResponse> getById(@PathVariable UUID id) {
        var startTime = currentTimeMillis();
        var response = service.getById(id);
        var getById = ResponseEntity.ok(response);
        LOGGER.info("M GET /products/ID {} : Returned product ID: {} in {}ms", getById.getStatusCode(), requireNonNull(getById.getBody()).id(), currentTimeMillis() - startTime);
        return getById;
    }

    @GetMapping
    @Operation(summary = "Get all products", description = "Retrieve a page of all registered products")
    ResponseEntity<Page<ProductResponse>> getAll(@PageableDefault(sort = "title", direction = Sort.Direction.ASC) Pageable pageable) {
        var startTime = currentTimeMillis();
        var responses = service.getAll(pageable);
        var getAll = ResponseEntity.ok(responses);
        LOGGER.info("M GET /products {} : Returned {} products in {}ms", getAll.getStatusCode(), requireNonNull(getAll.getBody()).getTotalElements(), currentTimeMillis() - startTime);
        return getAll;
    }

    @GetMapping("/search")
    @Operation(summary = "Get all searched products", description = "Retrieve a page of all registered products, searched by name or description")
    ResponseEntity<Page<ProductResponse>> getAllByNameOrDescription(@RequestParam("q") String query,
                                                                    @PageableDefault(sort = "title", direction = Sort.Direction.ASC) Pageable pageable) {
        var startTime = currentTimeMillis();
        var responses = service.getAllByNameOrDescription(query, pageable);
        var getAllByNameOrDescription = ResponseEntity.ok(responses);
        LOGGER.info("M GET /products/search {} : Returned {} products in {}ms", getAllByNameOrDescription.getStatusCode(), requireNonNull(getAllByNameOrDescription.getBody()).getTotalElements(), currentTimeMillis() - startTime);
        return getAllByNameOrDescription;
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a product", description = "Update the data of an existing product based on its ID")
    ResponseEntity<ProductResponse> update(@PathVariable UUID id,
                                           @Valid @RequestBody ProductRequest updateRequest) {
        var startTime = currentTimeMillis();
        var product = service.update(id, updateRequest);
        var response = mapper.toResponse(product);
        var put = ResponseEntity.ok(response);
        LOGGER.info("M PUT /products/ID {} : Updated product ID: {} in {}ms", put.getStatusCode(), requireNonNull(put.getBody()).id(), currentTimeMillis() - startTime);
        return put;
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a product", description = "Delete an existing product based on its ID")
    ResponseEntity<String> delete(@PathVariable UUID id) {
        var startTime = currentTimeMillis();
        service.delete(id);
        var delete = ResponseEntity.ok("Product deleted successfully");
        LOGGER.info("M DELETE /products/ID {} : Deleted product ID: {} in {}ms", delete.getStatusCode(), id, currentTimeMillis() - startTime);
        return delete;
    }
}