package com.github.wesleybritovlk.leasingserviceapi.user.address;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.ZonedDateTime;

@ToString
@Builder
@Getter
@EqualsAndHashCode
@Entity(name = "tb_address")
public class Address {
    @Id
    @Column(nullable = false, name = "postal_code", length = 9)
    private String postalCode;
    @Column(length = 100, nullable = false)
    private String street;
    @Column(length = 10)
    private String houseNumber;
    @Column(length = 50)
    private String complement;
    @Column(length = 50, nullable = false)
    private String district;
    @Column(length = 50, nullable = false)
    private String city;
    @Column(length = 2, nullable = false)
    private String state;
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private ZonedDateTime createdAt;
    @UpdateTimestamp
    @Column(nullable = false)
    private ZonedDateTime updatedAt;

    protected Address() {
    }

    protected Address(String postalCode,
                      String street,
                      String houseNumber,
                      String complement,
                      String district,
                      String city,
                      String state,
                      ZonedDateTime createdAt,
                      ZonedDateTime updatedAt) {
        this.postalCode = postalCode;
        this.street = street;
        this.houseNumber = houseNumber;
        this.complement = complement;
        this.district = district;
        this.city = city;
        this.state = state;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}