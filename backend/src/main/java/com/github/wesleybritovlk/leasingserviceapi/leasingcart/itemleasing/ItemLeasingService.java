package com.github.wesleybritovlk.leasingserviceapi.leasingcart.itemleasing;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface ItemLeasingService {
    ItemLeasing create(ItemLeasingRequestCreate requestCreate);

    ItemLeasingResponse findById(UUID id);

    Page<ItemLeasingResponse> findAll(Pageable pageable);

    ItemLeasing update(UUID id, ItemLeasingRequestUpdate requestUpdate);

    void delete(UUID id);
}