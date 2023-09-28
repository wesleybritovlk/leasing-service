package com.github.wesleybritovlk.leasingserviceapi.product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface ProductService {
    Product create(ProductRequest creationRequest);

    ProductResponse findById(UUID id);

    Page<ProductResponse> findAll(Pageable pageable);

    Page<ProductResponse> findAllByNameOrDescription(String query, Pageable pageable);

    Product update(UUID id, ProductRequest updateRequest);

    void delete(UUID id);
}