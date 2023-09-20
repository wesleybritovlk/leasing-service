package com.github.wesleybritovlk.leasingserviceapi.util.viacep;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;

@Disabled
@SpringBootTest
class ViaCepServiceTest {
    private final ViaCepService viaCepService;

    @Autowired
    ViaCepServiceTest(ViaCepService viaCepService) {
        this.viaCepService = viaCepService;
    }

    @Test
    void itShouldBeConsumedAndCollectedTheApiViaCepServiceWithOpenFeign() {
        // given
        String givenCep = "06763-490";
        // when
        ViaCepResponse cepResponse = viaCepService.findCepById(givenCep);
        // then
        assertThat(cepResponse).isNotNull();
        assertThat(cepResponse.cep()).isEqualTo(givenCep);
        assertThatThrownBy(() -> viaCepService.findCepById(any(String.class)))
                .hasMessageContaining("ViaCEP 400");
    }
}