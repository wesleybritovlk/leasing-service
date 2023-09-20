package com.github.wesleybritovlk.leasingserviceapi.exception;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.Spliterator;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@AutoConfigureTestEntityManager
class ApiExceptionRepositoryTest {
    private final ApiExceptionRepository underTest;
    private ApiException apiException;
    private ApiException apiException2;

    @Autowired
    ApiExceptionRepositoryTest(ApiExceptionRepository underTest) {
        this.underTest = underTest;
    }

    @BeforeEach
    void setUp() {
        apiException = ApiException.builder().createdAt(ZonedDateTime.now(ZoneId.of("America/Sao_Paulo"))).status(409).message("Conflict").requestPath("com/github/wesleybritovlk/leasingserviceapi/exception/ApiExceptionRepositoryTest.java").build();
        apiException2 = ApiException.builder().createdAt(ZonedDateTime.now(ZoneId.of("America/Sao_Paulo"))).status(400).message("Bad Request").requestPath("com/github/wesleybritovlk/leasingserviceapi/exception/ApiExceptionRepositoryTest.java").build();
    }

    @AfterEach
    void tearDown() {
        underTest.deleteAll();
    }

    @Test
    void itShouldSaveApiException() {
        // when
        Integer expectedStatus = 400;
        ApiException save = underTest.save(apiException2);
        // then
        assertThat(save.getStatus()).isEqualTo(expectedStatus);
        assertThat(save.getId()).isNotNull();
        assertThat(save.getStatus()).isNotNull();
        assertThat(save.getMessage()).isNotNull();
        assertThat(save.getRequestPath()).isNotNull();
    }

    @Test
    void itShouldFindApiExceptionById() {
        // given
        underTest.save(apiException);
        // when
        UUID expectedId = apiException.getId();
        Optional<ApiException> expectedApiException = Optional.of(apiException);
        Optional<ApiException> findApiException = underTest.findById(expectedId);
        // then
        assertThat(findApiException).isEqualTo(expectedApiException);
        assertThat(findApiException.get().getId()).isEqualTo(expectedId);
        assertThat(findApiException).isNotEqualTo(apiException);
        assertThat(findApiException).isNotNull();
    }

    @Test
    void itShouldFindAllApiExceptionByStatus() {
        // given
        underTest.save(apiException);
        underTest.save(apiException2);
        // when
        Long expectedSize = 1L;
        Integer expectedStatus = 409;
        Spliterator<ApiException> findAllApiExceptions = underTest.searchApiExceptionsByStatus(expectedStatus).spliterator();
        // then
        assertThat(findAllApiExceptions).isNotNull();
        assertThat(findAllApiExceptions.estimateSize()).isEqualTo(expectedSize);
    }

    @Test
    void itShouldDeleteApiException() {
        // given
        underTest.save(apiException);
        // when
        underTest.delete(apiException);
        Optional<ApiException> apiExceptionDeleted = underTest.findById(apiException.getId());
        // then
        assertThat(apiExceptionDeleted).isNotPresent();
        assertThat(apiExceptionDeleted).isNotEqualTo(apiException);
    }
}