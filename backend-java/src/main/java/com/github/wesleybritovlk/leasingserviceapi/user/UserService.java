package com.github.wesleybritovlk.leasingserviceapi.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.UUID;

public interface UserService {
    User create(UserRequest creationRequest);

    UserResponse findById(UUID id);

    Page<UserResponse> findAll(Pageable pageable);

    Page<UserResponse> findAllByCpf(String cpf, Pageable pageable);

    Page<UserResponse> findAllByNameAndDateOfBirth(String name, LocalDate dateOfBirth, Pageable pageable);

    User update(UUID id, UserRequest updateRequest);

    void delete(UUID id);
}