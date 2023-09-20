package com.github.wesleybritovlk.leasingserviceapi.leasingcart.itemleasing;

import com.github.wesleybritovlk.leasingserviceapi.leasingcart.LeasingCart;
import com.github.wesleybritovlk.leasingserviceapi.product.Product;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.UUID;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.AUTO;

@ToString
@Builder
@Getter
@Entity(name = "tb_item_leasing")
public class ItemLeasing implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = AUTO)
    private UUID id;
    @Setter
    @Column(nullable = false)
    private BigInteger quantityToLeased;
    @Getter
    @Setter
    @Column(nullable = false)
    private BigDecimal subtotalPrice;
    @Column(nullable = false)
    private ZonedDateTime expirationDate;
    @Setter
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "leasing_cart_fk", foreignKey = @ForeignKey(name = "tb_item_leasing_leasing_cart_fk"), nullable = false, referencedColumnName = "id")
    private LeasingCart leasingCart;
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "product_fk", foreignKey = @ForeignKey(name = "tb_item_leasing_product_fk"), nullable = false, updatable = false, referencedColumnName = "id")
    private Product product;
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private ZonedDateTime createdAt;
    @UpdateTimestamp
    @Column(nullable = false)
    private ZonedDateTime updatedAt;

    protected ItemLeasing() {
    }

    protected ItemLeasing(UUID id, BigInteger quantityToLeased, BigDecimal subtotalPrice, ZonedDateTime expirationDate, LeasingCart leasingCart, Product product, ZonedDateTime createdAt, ZonedDateTime updatedAt) {
        this.id = id;
        this.quantityToLeased = quantityToLeased;
        this.subtotalPrice = subtotalPrice;
        this.expirationDate = expirationDate;
        this.leasingCart = leasingCart;
        this.product = product;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    /**
     * ATTENTION leasingCart and product cannot be included in equals and hashCode methods.
     * Author: wesleybritovlk.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ItemLeasing that = (ItemLeasing) o;
        return Objects.equals(id, that.id) && Objects.equals(quantityToLeased, that.quantityToLeased) && Objects.equals(subtotalPrice, that.subtotalPrice) && Objects.equals(expirationDate, that.expirationDate) && Objects.equals(createdAt, that.createdAt) && Objects.equals(updatedAt, that.updatedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, quantityToLeased, subtotalPrice, expirationDate, createdAt, updatedAt);
    }
}