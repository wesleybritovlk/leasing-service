package com.github.wesleybritovlk.leasingserviceapi.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    @Query("SELECT u FROM tb_user u WHERE u.cpf = :cpf")
    Page<User> searchByCpf(String cpf, Pageable pageable);

    @Query("SELECT u " +
            "FROM tb_user u " +
            "WHERE LOWER(u.fullName) " +
            "LIKE LOWER(CONCAT('%',:fullName,'%')) " +
            "AND u.dateOfBirth = :dateOfBirth ")
    Page<User> searchByNameAndDateOfBirth(String fullName, LocalDate dateOfBirth, Pageable pageable);
}