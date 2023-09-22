package com.github.wesleybritovlk.leasingserviceapi.user.address;

import com.github.wesleybritovlk.leasingserviceapi.util.viacep.ViaCepResponse;
import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class AddressPoliticTest {

    @Test
    void itShouldMapperAddressRequestToAddress() {
        // given
        String addressRequest = "string";
        String cepResponse = "Garden Name";
        // when
        String validatedAddress = AddressPolitic.validateMapper(addressRequest, cepResponse);
        String validatedAddress1 = AddressPolitic.validateMapper(addressRequest, null);
        // then
        assertThat(validatedAddress).isNotBlank();
        assertThat(validatedAddress).isNotEqualTo("string");
        assertThat(validatedAddress).isNotEqualTo("AZ");
        assertThat(validatedAddress).isEqualTo(cepResponse);
        assertThat(validatedAddress1).isNotBlank();
        assertThat(validatedAddress1).isEqualTo("string");
    }

    @Test
    void itShouldValidatePostalCodeOfCepService() {
        // given
        AddressRequest addressRequest = new AddressRequest(null, "string", null, null, null, null, null);
        AddressRequest addressRequest1 = new AddressRequest(null, "", null, null, null, null, null);
        // when
        ViaCepResponse viaCepResponse = new ViaCepResponse(null, null, null, null, null, null, null, null, null, null);
        // then
        assertThatThrownBy(() -> AddressPolitic.validateCepService(addressRequest.street(), viaCepResponse.cep()))
                .isInstanceOf(ResponseStatusException.class).hasMessageContaining("Invalid postal code, please check it and try again");
        assertThatThrownBy(() -> AddressPolitic.validateCepService(addressRequest1.street(), viaCepResponse.cep()))
                .isInstanceOf(ResponseStatusException.class).hasMessageContaining("Invalid postal code, please check it and try again");
    }
}