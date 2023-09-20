package com.github.wesleybritovlk.leasingserviceapi.product;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.UUID;

public record ProductResponse(
        UUID id,
        String title,
        String description,
        BigInteger quantity,
        BigDecimal price,
        Integer validityDays,
        List<String> images
) {
}