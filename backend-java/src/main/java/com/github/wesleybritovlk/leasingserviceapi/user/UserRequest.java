package com.github.wesleybritovlk.leasingserviceapi.user;

import com.github.wesleybritovlk.leasingserviceapi.user.address.AddressRequest;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;

@Validated
public record UserRequest(
        @NotNull(message = "username shouldn't be null")
        @Size(min = 3, max = 50, message = "title must be greater than 3 and up to 50 characters")
        String fullName,
        @NotNull(message = "cpf shouldn't be null")
        @Pattern(regexp = "(\\d{11})$", message = "Invalid CPF, only numbers")
        String cpf,
        @NotNull(message = "Invalid or null date of birth. Format: yyyy-MM-dd")
        LocalDate dateOfBirth,
        @Email(regexp = "^([a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z]{2,50}(?:\\.[a-zA-Z]{2,50})?)$",
                message = "invalid email address")
        String email,
        @Pattern(regexp = "(\\d{2})9(\\d{8})$", message = "invalid mobile number entered ")
        String mobile,
        @NotNull(message = "address shouldn't be null")
        AddressRequest address,
        String image
) {
}