package com.github.wesleybritovlk.leasingserviceapi.product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface ProductService {
    Product create(ProductRequest creationRequest);

    ProductResponse getById(UUID id);

    Page<ProductResponse> getAll(Pageable pageable);

    Page<ProductResponse> getAllByNameOrDescription(String query, Pageable pageable);

    Product update(UUID id, ProductRequest updateRequest);

    void delete(UUID id);
}