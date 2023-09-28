package com.github.wesleybritovlk.leasingserviceapi.product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {
    @Query("SELECT p " +
            "FROM tb_product p " +
            "WHERE LOWER(p.title) " +
            "LIKE LOWER(CONCAT('%', :title, '%')) " +
            "OR LOWER(p.description) " +
            "LIKE LOWER(CONCAT('%', :description, '%'))")
    Page<Product> searchProductsByNameOrDescription(String title, String description, Pageable pageable);

    boolean existsByTitle(String title);
}