package com.github.wesleybritovlk.leasingserviceapi.user.address;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.Length;
import org.springframework.validation.annotation.Validated;

@Validated
public record AddressRequest(
        @NotNull(message = "Postal Code cannot be null")
        @NotBlank(message = "Postal Code cannot be blank")
        @Pattern(regexp = "\\d{8}$")
        @Length(min = 8, max = 8, message = "Invalid postal code, only 8 numbers")
        String postalCode,
        @NotNull(message = "Street cannot be null")
        @Size(max = 100)
        String street,
        @NotNull(message = "House number cannot be null")
        @NotBlank(message = "House number cannot be blank")
        @Size(max = 10)
        String number,
        @Size(max = 50)
        String complement,
        @NotNull(message = "District cannot be null")
        @Size(max = 50)
        String district,
        @NotNull(message = "City cannot be null")
        @Size(max = 50)
        String city,
        @NotNull(message = "State cannot be null")
        @Length(min = 2, max = 2, message = "Invalid state format")
        @Pattern(regexp = "AZ")
        String state
) {
}