package com.github.wesleybritovlk.leasingserviceapi.user.address;

import org.springframework.web.server.ResponseStatusException;

import static org.springframework.http.HttpStatus.NOT_FOUND;

public class AddressPolitic {
    protected static void validateCepService(String streetRequest, String cepResponse) {
        if (cepResponse == null && streetRequest.matches("\\s*|string"))
            throw new ResponseStatusException(NOT_FOUND, "Invalid postal code, please check it and try again");
    }

    protected static String validateMapper(String addressRequest, String cepResponse) {
        return cepResponse != null && addressRequest.matches("\\s*|string|AZ") ? cepResponse : addressRequest;
    }
}