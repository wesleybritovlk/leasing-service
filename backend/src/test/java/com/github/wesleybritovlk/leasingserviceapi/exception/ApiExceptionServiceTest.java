package com.github.wesleybritovlk.leasingserviceapi.exception;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ApiExceptionServiceTest {
    private ApiExceptionService apiExceptionService;
    @Mock
    private ApiExceptionRepository apiExceptionRepository;
    private ApiException apiException;
    private ApiExceptionResponse apiExceptionResponse;

    @BeforeEach
    void setUp() {
        apiExceptionService = new ApiExceptionService(apiExceptionRepository);
        apiExceptionResponse = new ApiExceptionResponse(ZonedDateTime.now(ZoneId.of("America/Sao_Paulo")), HttpStatus.CONFLICT, "Conflict", "com/github/wesleybritovlk/leasingserviceapi/exception/ApiExceptionServiceTest.java");
        apiException = ApiException.builder().id(UUID.randomUUID()).createdAt(apiExceptionResponse.timestamp()).status(HttpStatus.CONFLICT.value()).message("Conflict").requestPath("com/github/wesleybritovlk/leasingserviceapi/exception/ApiExceptionServiceTest.java").build();
    }

    @Test
    void itShouldCreateApiExceptionByApiExceptionDTO() {
        // when
        when(apiExceptionRepository.save(any(ApiException.class))).thenReturn(apiException);
        ApiException createdApiException = apiExceptionService.create(apiExceptionResponse);
        // then
        verify(apiExceptionRepository, times(1)).save(any(ApiException.class));
        assertThat(createdApiException).isNotNull();
        assertThat(createdApiException.getId()).isNotNull();
        assertThat(createdApiException.getCreatedAt()).isNotNull();
        assertThat(createdApiException.getStatus()).isEqualTo(apiExceptionResponse.status().value());
        assertThat(createdApiException.getMessage()).isEqualTo(apiExceptionResponse.message());
        assertThat(createdApiException.getRequestPath()).isEqualTo(apiExceptionResponse.path());
    }
}