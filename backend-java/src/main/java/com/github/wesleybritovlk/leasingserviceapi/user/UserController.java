package com.github.wesleybritovlk.leasingserviceapi.user;

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

import java.time.LocalDate;
import java.util.UUID;

import static java.lang.System.currentTimeMillis;
import static java.util.Objects.requireNonNull;
import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/users")
@Tag(name = "Users Endpoint", description = "RESTful Endpoint for managing users.")
@RequiredArgsConstructor
class UserController {
    private final UserService service;
    private final UserMapper mapper;

    private static final Logger LOGGER = getLogger(UserController.class);

    @PostMapping
    @Operation(summary = "Create a new user", description = "Create a new user and return the created user's data")
    ResponseEntity<UserResponse> create(@Valid @RequestBody UserRequest creationRequest) {
        var startTime = currentTimeMillis();
        var user = service.create(creationRequest);
        var response = mapper.toResponse(user);
        var post = ResponseEntity.status(CREATED).body(response);
        LOGGER.info("M POST /users {} , Created user ID: {} in {}ms", post.getStatusCode(), requireNonNull(post.getBody()).id(), currentTimeMillis() - startTime);
        return post;
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a user by ID", description = "Retrieve a specific user based on its ID")
    ResponseEntity<UserResponse> getById(@PathVariable UUID id) {
        var startTime = currentTimeMillis();
        var response = service.findById(id);
        var getById = ResponseEntity.ok(response);
        LOGGER.info("M GET /users/ID {} : Returned user ID: {} in {}ms", getById.getStatusCode(), requireNonNull(getById.getBody()).id(), currentTimeMillis() - startTime);
        return getById;
    }

    @GetMapping
    @Operation(summary = "Get all users", description = "Retrieve a page of all registered users")
    ResponseEntity<Page<UserResponse>> getAll(@PageableDefault(size = 5, sort = "name", direction = Sort.Direction.ASC) Pageable pageable) {
        var startTime = currentTimeMillis();
        var responses = service.findAll(pageable);
        var getAll = ResponseEntity.ok(responses);
        LOGGER.info("M GET /users {} : Returned {} users in {}ms", getAll.getStatusCode(), requireNonNull(getAll.getBody()).getTotalElements(), currentTimeMillis() - startTime);
        return getAll;
    }

    @GetMapping("/search")
    @Operation(summary = "Get all searched users", description = "Retrieve a page of all registered users, searched by cpf or name and date of birth")
    ResponseEntity<Page<UserResponse>> getAllByCpfOrNameAndDateOfBirth(@RequestParam(name = "cpf", required = false) String cpf,
                                                                       @RequestParam(name = "name", required = false) String name,
                                                                       @RequestParam(name = "date_of_birth", required = false) LocalDate dateOfBirth,
                                                                       @PageableDefault(size = 5, sort = "name", direction = Sort.Direction.ASC) Pageable pageable) {
        var startTime = currentTimeMillis();
        Page<UserResponse> responses;
        if (cpf == null || cpf.isBlank())
            responses = service.findAllByNameAndDateOfBirth(name, dateOfBirth, pageable);
        else responses = service.findAllByCpf(cpf, pageable);
        var getAllByCpfOrNameAndDateOfBirth = ResponseEntity.ok(responses);
        LOGGER.info("M GET /users/search {} : Returned {} users in {}ms", getAllByCpfOrNameAndDateOfBirth.getStatusCode(), requireNonNull(getAllByCpfOrNameAndDateOfBirth.getBody()).getTotalElements(), currentTimeMillis() - startTime);
        return getAllByCpfOrNameAndDateOfBirth;
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a user", description = "Update the data of an existing user based on its ID")
    ResponseEntity<UserResponse> update(@PathVariable UUID id,
                                        @Valid @RequestBody UserRequest updateRequest) {
        var startTime = currentTimeMillis();
        var user = service.update(id, updateRequest);
        var userResponse = mapper.toResponse(user);
        var put = ResponseEntity.ok(userResponse);
        LOGGER.info("M PUT /users/ID {} : Updated product ID: {} in {}ms", put.getStatusCode(), requireNonNull(put.getBody()).id(), currentTimeMillis() - startTime);
        return put;
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a user", description = "Delete an existing user based on its ID")
    ResponseEntity<String> delete(@PathVariable UUID id) {
        var startTime = currentTimeMillis();
        service.delete(id);
        ResponseEntity<String> delete = ResponseEntity.ok("User deleted successfully");
        LOGGER.info("M DELETE /products/ID {} : Deleted product ID: {} in {}ms", delete.getStatusCode(), id, currentTimeMillis() - startTime);
        return delete;
    }
}