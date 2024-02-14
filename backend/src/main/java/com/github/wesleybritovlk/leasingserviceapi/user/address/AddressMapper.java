package com.github.wesleybritovlk.leasingserviceapi.user.address;

import com.github.wesleybritovlk.leasingserviceapi.util.viacep.ViaCepResponse;

public interface AddressMapper {
    Address toAddress(AddressRequest requestCreate, ViaCepResponse cepResponse);
    Address toAddress(Address findAddress, AddressRequest requestUpdate, ViaCepResponse cepResponse);
    AddressResponse toResponse(Address address);
}