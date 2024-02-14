package com.github.wesleybritovlk.leasingserviceapi.exception;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;

import java.io.Serial;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.UUID;

import static jakarta.persistence.GenerationType.AUTO;

@Builder
@Getter
@EqualsAndHashCode
@Entity(name = "tb_api_exception")
class ApiException implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = AUTO)
    private UUID id;
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private ZonedDateTime createdAt;
    @Column(nullable = false)
    private Integer status;
    @Column(nullable = false)
    private String message;
    @Column(nullable = false)
    private String requestPath;

    protected ApiException() {
    }

    protected ApiException(UUID id, ZonedDateTime createdAt, Integer status, String message, String requestPath) {
        this.id = id;
        this.createdAt = createdAt;
        this.status = status;
        this.message = message;
        this.requestPath = requestPath;
    }
}