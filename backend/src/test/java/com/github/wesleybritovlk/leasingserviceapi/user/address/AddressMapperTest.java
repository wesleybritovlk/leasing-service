package com.github.wesleybritovlk.leasingserviceapi.user.address;

import com.github.wesleybritovlk.leasingserviceapi.util.viacep.ViaCepResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(MockitoExtension.class)
class AddressMapperTest {
    private AddressMapper mapper;
    private Address address;
    private AddressRequest addressRequest;
    private ViaCepResponse viaCepResponse;

    @BeforeEach
    void setUp() {
        mapper = new AddressMapperImpl();
        address = Address.builder().postalCode("0111222").street("Street Name").houseNumber("000").complement("N/A").district("Garden Name").city("City Name").state("UF").build();
        addressRequest = new AddressRequest("01000200", "", "000", "Apto 000", "Garden Name", "São Paulo", "SP");
        viaCepResponse = new ViaCepResponse("Street Name", "Rua dos Coders Official", null, "Garden Name", "São Paulo", "SP", null, null, null, null);
    }

    @Test
    void itShouldBuildTheToAddressWithCreationRequest() {
        // given
        AddressRequest creationRequest = addressRequest;
        // when
        Address toAddressCreated = mapper.toAddress(creationRequest, viaCepResponse);
        // then
        assertThat(toAddressCreated).isNotNull();
        assertThat(toAddressCreated.getPostalCode()).isEqualTo(creationRequest.postalCode());
        assertThat(toAddressCreated.getStreet()).isEqualTo(viaCepResponse.logradouro());
        assertThat(toAddressCreated.getHouseNumber()).isEqualTo(creationRequest.number());
        assertThat(toAddressCreated.getComplement()).isEqualTo(creationRequest.complement());
        assertThat(toAddressCreated.getDistrict()).isEqualTo(creationRequest.district());
        assertThat(toAddressCreated.getCity()).isEqualTo(creationRequest.city());
        assertThat(toAddressCreated.getState()).isEqualTo(creationRequest.state());
    }

    @Test
    void itShouldBuildTheToUserWithFindUserRepositoryAndUpdateRequest() {
        // given
        Address findAddress = address;
        AddressRequest updateRequest = addressRequest;
        // when
        Address toAddressUpdated = mapper.toAddress(findAddress, updateRequest, viaCepResponse);
        // then
        assertThat(toAddressUpdated).isNotNull();
        assertThat(toAddressUpdated.getPostalCode()).isEqualTo(updateRequest.postalCode());
        assertThat(toAddressUpdated.getStreet()).isEqualTo(viaCepResponse.logradouro());
        assertThat(toAddressUpdated.getHouseNumber()).isEqualTo(updateRequest.number());
        assertThat(toAddressUpdated.getComplement()).isEqualTo(updateRequest.complement());
        assertThat(toAddressUpdated.getDistrict()).isEqualTo(updateRequest.district());
        assertThat(toAddressUpdated.getCity()).isEqualTo(updateRequest.city());
        assertThat(toAddressUpdated.getState()).isEqualTo(updateRequest.state());
    }

    @Test
    void itShouldReturnUserResponseFromUser() {
        // when
        AddressResponse addressResponse = mapper.toResponse(address);
        // then
        assertThat(addressResponse).isNotNull();
        assertThat(addressResponse.postalCode()).isEqualTo(address.getPostalCode());
        assertThat(addressResponse.street()).isEqualTo(address.getStreet());
        assertThat(addressResponse.number()).isEqualTo(address.getHouseNumber());
        assertThat(addressResponse.complement()).isEqualTo(address.getComplement());
        assertThat(addressResponse.district()).isEqualTo(address.getDistrict());
        assertThat(addressResponse.city()).isEqualTo(address.getCity());
        assertThat(addressResponse.state()).isEqualTo(address.getState());
    }
}