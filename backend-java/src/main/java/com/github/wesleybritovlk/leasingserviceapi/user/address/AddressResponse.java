package com.github.wesleybritovlk.leasingserviceapi.user.address;

import org.springframework.validation.annotation.Validated;

@Validated
public record AddressResponse(
        String postalCode,
        String street,
        String number,
        String complement,
        String district,
        String city,
        String state
) {
}