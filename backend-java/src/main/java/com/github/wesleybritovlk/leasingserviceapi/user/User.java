package com.github.wesleybritovlk.leasingserviceapi.user;

import com.github.wesleybritovlk.leasingserviceapi.leasingcart.LeasingCart;
import com.github.wesleybritovlk.leasingserviceapi.user.address.Address;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.UUID;

import static jakarta.persistence.FetchType.EAGER;
import static jakarta.persistence.FetchType.LAZY;

@ToString
@Builder
@Getter
@EqualsAndHashCode
@Entity(name = "tb_user")
@Table(uniqueConstraints = {@UniqueConstraint(name = "tb_user_cpf_uk", columnNames = {"cpf"}),
        @UniqueConstraint(name = "tb_user_email_uk", columnNames = {"email"}),
        @UniqueConstraint(name = "tb_user_mobile_uk", columnNames = {"mobile"})})
public class User implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @Column(nullable = false, length = 50)
    private String fullName;
    @Column(nullable = false, length = 11)
    private String cpf;
    @Column(nullable = false)
    private LocalDate dateOfBirth;
    @Column(nullable = false, length = 50)
    private String email;
    @Column(nullable = false, length = 11)
    private String mobile;
    @ManyToOne(fetch = EAGER, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "address_fk", foreignKey = @ForeignKey(name = "tb_user_address_fk"),
            nullable = false, referencedColumnName = "postal_code")
    private Address address;
    private String imageUrl;
    @OneToOne(fetch = LAZY, cascade = CascadeType.ALL, mappedBy = "user")
    private LeasingCart leasingCart;
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private ZonedDateTime createdAt;
    @UpdateTimestamp
    @Column(nullable = false)
    private ZonedDateTime updatedAt;

    protected User() {
    }

    protected User(UUID id,
                   String fullName,
                   String cpf,
                   LocalDate dateOfBirth,
                   String email,
                   String mobile,
                   Address address,
                   String imageUrl,
                   LeasingCart leasingCart,
                   ZonedDateTime createdAt,
                   ZonedDateTime updatedAt) {
        this.id = id;
        this.fullName = fullName;
        this.cpf = cpf;
        this.dateOfBirth = dateOfBirth;
        this.email = email;
        this.mobile = mobile;
        this.address = address;
        this.imageUrl = imageUrl;
        this.leasingCart = leasingCart;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}