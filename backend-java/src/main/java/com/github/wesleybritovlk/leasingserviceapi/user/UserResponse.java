package com.github.wesleybritovlk.leasingserviceapi.user;

import com.github.wesleybritovlk.leasingserviceapi.user.address.AddressResponse;

import java.time.LocalDate;
import java.util.UUID;

public record UserResponse(
        UUID id,
        String fullName,
        String cpf,
        LocalDate dateOfBirth,
        String email,
        String mobile,
        AddressResponse address,
        String image
) {
}