package com.github.wesleybritovlk.leasingserviceapi.user.address;

public interface AddressService {
    Address create(AddressRequest addressRequest);
    Address update(Address findAddress, AddressRequest addressRequest);
    AddressResponse findByAddress(Address address);
}