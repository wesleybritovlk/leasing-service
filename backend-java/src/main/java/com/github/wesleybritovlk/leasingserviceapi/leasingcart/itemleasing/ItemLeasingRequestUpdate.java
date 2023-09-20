package com.github.wesleybritovlk.leasingserviceapi.leasingcart.itemleasing;

import jakarta.validation.constraints.Min;
import org.springframework.validation.annotation.Validated;

import java.math.BigInteger;

@Validated
public record ItemLeasingRequestUpdate(
        @Min(value = 1, message = "Quantity is required") BigInteger quantity
) {
}