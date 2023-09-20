package com.github.wesleybritovlk.leasingserviceapi.leasingcart;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface LeasingCartService {
    LeasingCart create(LeasingCartRequest requestCreate);

    LeasingCartResponse findById(UUID id);

    Page<LeasingCartResponse> findAll(Pageable pageable);

    LeasingCart update(UUID id, LeasingCartRequest requestUpdate);

    void delete(UUID id);
}