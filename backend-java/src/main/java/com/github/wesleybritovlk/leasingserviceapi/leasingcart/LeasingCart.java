package com.github.wesleybritovlk.leasingserviceapi.leasingcart;

import com.github.wesleybritovlk.leasingserviceapi.leasingcart.itemleasing.ItemLeasing;
import com.github.wesleybritovlk.leasingserviceapi.user.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Set;
import java.util.UUID;

import static jakarta.persistence.CascadeType.ALL;
import static jakarta.persistence.FetchType.EAGER;
import static jakarta.persistence.FetchType.LAZY;

@ToString
@Builder
@Getter
@EqualsAndHashCode
@Entity(name = "tb_leasing_cart")
@Table(uniqueConstraints = @UniqueConstraint(name = "tb_leasing_cart_user_fk_uk", columnNames = {"user_fk"}))
public class LeasingCart implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @Setter
    @Column(nullable = false)
    private BigDecimal totalPrice;
    @Setter
    @Column(nullable = false)
    private ZonedDateTime firstExpirationDate;
    @Setter
    @Column(nullable = false)
    private ZonedDateTime lastExpirationDate;
    @OneToMany(fetch = EAGER, cascade = ALL)
    private Set<ItemLeasing> itemsLeasing;
    @OneToOne(fetch = LAZY, optional = false)
    @JoinColumn(name = "user_fk", foreignKey = @ForeignKey(name = "tb_leasing_cart_user_fk"),
            nullable = false, referencedColumnName = "id")
    private User user;
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private ZonedDateTime createdAt;
    @UpdateTimestamp
    @Column(nullable = false)
    private ZonedDateTime updatedAt;

    protected LeasingCart() {
    }

    protected LeasingCart(UUID id,
                          BigDecimal totalPrice,
                          ZonedDateTime firstExpirationDate,
                          ZonedDateTime lastExpirationDate,
                          Set<ItemLeasing> itemsLeasing,
                          User user,
                          ZonedDateTime createdAt,
                          ZonedDateTime updatedAt) {
        this.id = id;
        this.totalPrice = totalPrice;
        this.firstExpirationDate = firstExpirationDate;
        this.lastExpirationDate = lastExpirationDate;
        this.itemsLeasing = itemsLeasing;
        this.user = user;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}