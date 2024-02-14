package com.github.wesleybritovlk.leasingserviceapi.exception;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
interface ApiExceptionRepository extends JpaRepository<ApiException, UUID> {
    @Query("SELECT e FROM tb_api_exception e WHERE e.status = :status")
    List<ApiException> searchApiExceptionsByStatus(Integer status);
}