package com.github.wesleybritovlk.leasingserviceapi.leasingcart.itemleasing;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

import java.math.BigInteger;
import java.util.UUID;

@Validated
public record ItemLeasingRequestCreate(
        @NotNull(message = "Leasing id is required")
        UUID cart,
        @NotNull(message = "Product id is required")
        UUID product,
        @Min(value = 1, message = "Quantity is required")
        BigInteger quantity
) {
}

