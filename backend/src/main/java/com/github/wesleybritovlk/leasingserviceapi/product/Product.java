package com.github.wesleybritovlk.leasingserviceapi.product;

import com.github.wesleybritovlk.leasingserviceapi.leasingcart.itemleasing.ItemLeasing;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static jakarta.persistence.CascadeType.ALL;

@ToString
@Builder
@Getter
@EqualsAndHashCode
@Entity(name = "tb_product")
@Table(uniqueConstraints = @UniqueConstraint(name = "tb_product_title_uk", columnNames = {"title"}))
public class Product implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @Column(nullable = false, length = 100)
    private String title;
    @Column(nullable = false)
    private String description;
    @Column(nullable = false)
    private BigInteger quantityInStock;
    @Column(nullable = false)
    private BigDecimal price;
    @Column(nullable = false)
    private Integer validityNumberOfDays;
    private List<String> imagesUrl;
    @OneToMany(mappedBy = "product", cascade = ALL)
    private Set<ItemLeasing> itemsLeasing;
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private ZonedDateTime createdAt;
    @UpdateTimestamp
    @Column(nullable = false)
    private ZonedDateTime updatedAt;

    protected Product() {
    }

    protected Product(UUID id,
                      String title,
                      String description,
                      BigInteger quantityInStock,
                      BigDecimal price,
                      Integer validityNumberOfDays,
                      List<String> imagesUrl,
                      Set<ItemLeasing> itemsLeasing,
                      ZonedDateTime createdAt,
                      ZonedDateTime updatedAt) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.quantityInStock = quantityInStock;
        this.price = price;
        this.validityNumberOfDays = validityNumberOfDays;
        this.imagesUrl = imagesUrl;
        this.itemsLeasing = itemsLeasing;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    protected void setQuantityInStock(BigInteger quantityInStock) {
        this.quantityInStock = quantityInStock;
    }
}