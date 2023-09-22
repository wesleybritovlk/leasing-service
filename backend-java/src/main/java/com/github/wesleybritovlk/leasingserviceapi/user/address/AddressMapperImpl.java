package com.github.wesleybritovlk.leasingserviceapi.user.address;

import com.github.wesleybritovlk.leasingserviceapi.util.viacep.ViaCepResponse;
import org.springframework.stereotype.Component;

import static com.github.wesleybritovlk.leasingserviceapi.user.address.AddressPolitic.validateMapper;
import static java.time.ZoneId.of;
import static java.time.ZonedDateTime.now;

@Component
public class AddressMapperImpl implements AddressMapper {
    @Override
    public Address toAddress(AddressRequest requestCreate, ViaCepResponse cepResponse) {
        var street = validateMapper(requestCreate.street(), cepResponse.logradouro());
        var district = validateMapper(requestCreate.district(), cepResponse.bairro());
        var city = validateMapper(requestCreate.city(), cepResponse.localidade());
        var state = validateMapper(requestCreate.state(), cepResponse.uf());
        return Address.builder().postalCode(requestCreate.postalCode()).street(street).houseNumber(requestCreate.number()).complement(requestCreate.complement()).district(district).city(city).state(state).createdAt(now(of("America/Sao_Paulo"))).updatedAt(now(of("America/Sao_Paulo"))).build();
    }

    @Override
    public Address toAddress(Address findAddress, AddressRequest requestUpdate, ViaCepResponse cepResponse) {
        var street = validateMapper(requestUpdate.street(), cepResponse.logradouro());
        var district = validateMapper(requestUpdate.district(), cepResponse.bairro());
        var city = validateMapper(requestUpdate.city(), cepResponse.localidade());
        var state = validateMapper(requestUpdate.state(), cepResponse.uf());
        return Address.builder().postalCode(requestUpdate.postalCode()).street(street).houseNumber(requestUpdate.number()).complement(requestUpdate.complement()).district(district).city(city).state(state).createdAt(findAddress.getCreatedAt()).updatedAt(now(of("America/Sao_Paulo"))).build();
    }

    @Override
    public AddressResponse toResponse(Address address) {
        return new AddressResponse(address.getPostalCode(), address.getStreet(), address.getHouseNumber(), address.getComplement(), address.getDistrict(), address.getCity(), address.getState());
    }
}