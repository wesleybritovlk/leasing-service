package com.github.wesleybritovlk.leasingserviceapi.user.address;

import com.github.wesleybritovlk.leasingserviceapi.util.viacep.ViaCepResponse;
import com.github.wesleybritovlk.leasingserviceapi.util.viacep.ViaCepService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AddressServiceTest {
    private AddressService service;
    @Mock
    private AddressRepository repository;
    @Mock
    private AddressMapper mapper;
    @Mock
    private ViaCepService cepService;
    private Address address;
    private AddressRequest addressRequest;
    private ViaCepResponse viaCepResponse;

    @BeforeEach
    void setUp() {
        service = new AddressServiceImpl(repository, mapper, cepService);
        address = Address.builder().postalCode("01000200").street("Rua dos Coders Official").houseNumber("000").complement("Apto 000").district("Garden Name").city("São Paulo").state("SP").build();
        addressRequest = new AddressRequest("01000200", "", "000", "Apto 000", "Garden Name", "São Paulo", "SP");
        viaCepResponse = new ViaCepResponse("Street Name", "Rua dos Coders Official", null, "Garden Name", "São Paulo", "SP", null, null, null, null);
    }

    private static AddressResponse getAddressResponse(Address address) {
        return new AddressResponse(address.getPostalCode(), address.getStreet(), address.getHouseNumber(), address.getComplement(), address.getDistrict(), address.getCity(), address.getState());
    }

    @Test
    void itShouldCreateAddressByAddressRequest() {
        // when
        when(cepService.findCepById(any(String.class))).thenReturn(viaCepResponse);
        when(mapper.toAddress(any(AddressRequest.class), any(ViaCepResponse.class))).thenReturn(address);
        when(repository.save(any(Address.class))).thenReturn(address);
        Address createAddress = service.create(addressRequest);
        // then
        verify(cepService, times(1)).findCepById(any(String.class));
        verify(mapper, times(1)).toAddress(any(AddressRequest.class), any(ViaCepResponse.class));
        verify(repository, times(1)).save(any(Address.class));
        assertThat(createAddress.getPostalCode()).isEqualTo(addressRequest.postalCode());
        assertThat(createAddress.getStreet()).isEqualTo(viaCepResponse.logradouro());
        assertThat(createAddress.getHouseNumber()).isEqualTo(addressRequest.number());
        assertThat(createAddress.getComplement()).isEqualTo(addressRequest.complement());
        assertThat(createAddress.getDistrict()).isEqualTo(viaCepResponse.bairro());
        assertThat(createAddress.getCity()).isEqualTo(viaCepResponse.localidade());
        assertThat(createAddress.getState()).isEqualTo(viaCepResponse.uf());
    }

    @Test
    void itShouldUpdateAddressByAddressRequest() {
        // when
        when(cepService.findCepById(any(String.class))).thenReturn(viaCepResponse);
        when(mapper.toAddress(any(Address.class), any(AddressRequest.class), any(ViaCepResponse.class))).thenReturn(address);
        when(repository.save(any(Address.class))).thenReturn(address);
        Address createAddress = service.update(address, addressRequest);
        // then
        verify(cepService, times(1)).findCepById(any(String.class));
        verify(mapper, times(1)).toAddress(any(Address.class), any(AddressRequest.class), any(ViaCepResponse.class));
        verify(repository, times(1)).save(any(Address.class));
        assertThat(createAddress.getPostalCode()).isEqualTo(addressRequest.postalCode());
        assertThat(createAddress.getStreet()).isEqualTo(viaCepResponse.logradouro());
        assertThat(createAddress.getHouseNumber()).isEqualTo(addressRequest.number());
        assertThat(createAddress.getComplement()).isEqualTo(addressRequest.complement());
        assertThat(createAddress.getDistrict()).isEqualTo(viaCepResponse.bairro());
        assertThat(createAddress.getCity()).isEqualTo(viaCepResponse.localidade());
        assertThat(createAddress.getState()).isEqualTo(viaCepResponse.uf());
    }

    @Test
    void itShouldGetAddressResponseByAddress() {
        // when
        when(mapper.toResponse(any(Address.class))).thenReturn(getAddressResponse(address));
        AddressResponse findAddress = service.findByAddress(address);
        // then
        verify(mapper, times(1)).toResponse(any(Address.class));
        assertThat(findAddress).isNotNull();
        assertThat(findAddress.postalCode()).isEqualTo(address.getPostalCode());
        assertThat(findAddress.street()).isEqualTo(address.getStreet());
        assertThat(findAddress.number()).isEqualTo(address.getHouseNumber());
        assertThat(findAddress.complement()).isEqualTo(address.getComplement());
        assertThat(findAddress.district()).isEqualTo(address.getDistrict());
        assertThat(findAddress.city()).isEqualTo(address.getCity());
        assertThat(findAddress.state()).isEqualTo(address.getState());
    }
}