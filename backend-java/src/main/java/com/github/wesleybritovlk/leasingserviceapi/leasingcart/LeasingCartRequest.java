package com.github.wesleybritovlk.leasingserviceapi.leasingcart;

import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

import java.util.UUID;

@Validated
public record LeasingCartRequest(
        @NotNull(message = "User Id is required")
        UUID user
) {
}