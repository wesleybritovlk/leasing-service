package com.github.wesleybritovlk.leasingserviceapi.exception;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static java.lang.String.valueOf;

@Service
@RequiredArgsConstructor
class ApiExceptionService {
    private final ApiExceptionRepository repository;

    ApiException create(ApiExceptionResponse response) {
        var builder = ApiException.builder().createdAt(response.timestamp()).status(response.status().value()).message(valueOf(response.message())).requestPath(response.path());
        return repository.save(builder.build());
    }
}