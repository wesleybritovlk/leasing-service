package com.github.wesleybritovlk.leasingserviceapi.leasingcart.itemleasing;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static java.lang.System.currentTimeMillis;
import static java.util.Objects.requireNonNull;
import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.data.domain.Sort.Direction.DESC;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.http.ResponseEntity.status;

@RestController
@RequestMapping("/carts/items")
@Tag(name = "Items Leasing Endpoint", description = "RESTful Endpoint for managing items of Leasing Carts Endpoint.")
@RequiredArgsConstructor
class ItemLeasingController {
    private final ItemLeasingService service;
    private final ItemLeasingMapper mapper;

    private static final Logger LOGGER = getLogger(ItemLeasingController.class);

    @PostMapping
    @Operation(summary = "Create a new item", description = "Create a new item and return the created item's data")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Item created successfully"),
            @ApiResponse(responseCode = "422", description = "Invalid item data provided"),
            @ApiResponse(responseCode = "409", description = "The item quantity is greater than the stock quantity")
    })
    ResponseEntity<ItemLeasingResponse> create(@Valid @RequestBody ItemLeasingRequestCreate requestCreate) {
        var startTime = currentTimeMillis();
        var itemLeasing = service.create(requestCreate);
        var response = mapper.toResponse(itemLeasing);
        var post = status(CREATED).body(response);
        LOGGER.info("M POST /carts/items {} , Created item_leasing ID: {} in {}ms", post.getStatusCode(), requireNonNull(post.getBody()).id(), currentTimeMillis() - startTime);
        return post;
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a item by ID", description = "Retrieve a specific item based on its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operation successful"),
            @ApiResponse(responseCode = "404", description = "Item not found")
    })
    ResponseEntity<ItemLeasingResponse> getById(@PathVariable UUID id) {
        var startTime = currentTimeMillis();
        var response = service.findById(id);
        var getById = ok(response);
        LOGGER.info("M GET /carts/items/ID {} : Returned item_leasing ID: {} in {}ms", getById.getStatusCode(), requireNonNull(getById.getBody()).id(), currentTimeMillis() - startTime);
        return getById;
    }

    @GetMapping
    @Operation(summary = "Get all items", description = "Retrieve a page of all registered items")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operation successful"),
            @ApiResponse(responseCode = "400", description = "Invalid paging parameter")
    })
    ResponseEntity<Page<ItemLeasingResponse>> getAll(@PageableDefault(size = 5, sort = "updatedAt", direction = DESC) Pageable pageable) {
        var startTime = currentTimeMillis();
        var responses = service.findAll(pageable);
        var getAll = ok(responses);
        LOGGER.info("M GET /carts/items {} : Returned {} items_leasing in {}ms", getAll.getStatusCode(), requireNonNull(getAll.getBody()).getTotalElements(), currentTimeMillis() - startTime);
        return getAll;
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a item", description = "Update the data of an existing item based on its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Item updated successfully"),
            @ApiResponse(responseCode = "404", description = "Item not found"),
            @ApiResponse(responseCode = "422", description = "Invalid item data provided"),
            @ApiResponse(responseCode = "409", description = "The item quantity is greater than the stock quantity")
    })
    ResponseEntity<ItemLeasingResponse> update(@PathVariable UUID id,
                                               @Valid @RequestBody ItemLeasingRequestUpdate updateRequest) {
        var startTime = currentTimeMillis();
        var itemLeasing = service.update(id, updateRequest);
        var response = mapper.toResponse(itemLeasing);
        var put = ok(response);
        LOGGER.info("M PUT /carts/items/ID {} : Updated item_leasing ID: {} in {}ms", put.getStatusCode(), requireNonNull(put.getBody()).id(), currentTimeMillis() - startTime);
        return put;
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a item", description = "Delete an existing item based on its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Item deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Item not found")
    })
    ResponseEntity<String> delete(@PathVariable UUID id) {
        var startTime = currentTimeMillis();
        service.delete(id);
        var delete = ok("Item Leasing deleted successfully");
        LOGGER.info("M DELETE /carts/items/ID {} : Deleted item_leasing ID: {} in {}ms", delete.getStatusCode(), id, currentTimeMillis() - startTime);
        return delete;
    }
}