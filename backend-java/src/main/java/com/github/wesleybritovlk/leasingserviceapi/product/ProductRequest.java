package com.github.wesleybritovlk.leasingserviceapi.product;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

@Validated
public record ProductRequest(
        @NotBlank(message = "Product title is required")
        @Size(min = 5, max = 100, message = "Product title must be between 5 and 100 characters")
        String title,
        @NotBlank(message = "Product description is required")
        @Size(min = 5, max = 255, message = "Product description must be between 5 and 255 characters")
        String description,
        @DecimalMin(value = "1", message = "Quantity is required")
        BigInteger quantity,
        @DecimalMin(value = "0.01", message = "Product price is required")
        BigDecimal price,
        @DecimalMin(value = "1", message = "The Product needs at least one day of expiry")
        Integer validityDays,
        List<String> images
) {
}