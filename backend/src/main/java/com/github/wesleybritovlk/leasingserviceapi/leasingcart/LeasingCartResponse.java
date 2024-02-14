package com.github.wesleybritovlk.leasingserviceapi.leasingcart;

import com.github.wesleybritovlk.leasingserviceapi.leasingcart.itemleasing.ItemLeasingResponse;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Set;
import java.util.UUID;

public record LeasingCartResponse(
        UUID id,
        UUID user,
        String name,
        BigDecimal price,
        ZonedDateTime firstExpiration,
        ZonedDateTime lastExpiration,
        Set<ItemLeasingResponse> items
) {
}