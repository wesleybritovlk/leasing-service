package com.github.wesleybritovlk.leasingserviceapi.leasingcart.itemleasing;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.ZonedDateTime;
import java.util.UUID;

public record ItemLeasingResponse(
        UUID id,
        UUID cart,
        UUID product,
        String title,
        BigInteger quantity,
        BigDecimal price,
        ZonedDateTime expiration,
        String image
) {
}