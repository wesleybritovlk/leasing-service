package com.github.wesleybritovlk.leasingserviceapi.user.address;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.springframework.validation.annotation.Validated;

@Validated
public record AddressRequest(
        @NotNull(message = "Postal Code cannot be null")
        @Pattern(regexp = "\\d{8}$", message = "Invalid Postal Code, numbers only")
        String postalCode,
        @Size(max = 100)
        String street,
        @Size(max = 10)
        String number,
        @Size(max = 50)
        String complement,
        @Size(max = 50)
        String district,
        @Size(max = 50)
        String city,
        @Pattern(regexp = "A-Z{2}$")
        String state
) {
}