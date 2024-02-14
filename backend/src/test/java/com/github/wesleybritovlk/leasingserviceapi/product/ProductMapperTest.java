package com.github.wesleybritovlk.leasingserviceapi.product;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static java.math.BigInteger.valueOf;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class ProductMapperTest {
    private ProductMapper mapper;
    private Product product;
    private ProductRequest productRequest;

    @BeforeEach
    void setUp() {
        mapper = new ProductMapperImpl();
        product = Product.builder().id(UUID.randomUUID()).title("Name Test").description("Description Test").quantityInStock(valueOf(5)).price(BigDecimal.valueOf(15)).validityNumberOfDays(10).build();
        productRequest = new ProductRequest("Name Request Test", "Description Request Test", valueOf(10), BigDecimal.valueOf(10), 30, List.of(""));
    }

    @Test
    void itShouldBuildTheToProductWithCreationRequest() {
        // given
        ProductRequest creationRequest = productRequest;
        // when
        Product toProductCreated = mapper.toProduct(creationRequest);
        // then
        assertThat(toProductCreated).isNotNull();
        assertThat(toProductCreated.getTitle()).isEqualTo(creationRequest.title());
        assertThat(toProductCreated.getDescription()).isEqualTo(creationRequest.description());
        assertThat(toProductCreated.getQuantityInStock()).isEqualTo(creationRequest.quantity());
        assertThat(toProductCreated.getPrice()).isEqualTo(creationRequest.price());
        assertThat(toProductCreated.getValidityNumberOfDays()).isEqualTo(creationRequest.validityDays());
    }

    @Test
    void itShouldBuildTheToProductWithFindProductRepositoryAndUpdateRequest() {
        // given
        Product findProduct = product;
        ProductRequest updateRequest = productRequest;
        // when
        Product toProductUpdated = mapper.toProduct(findProduct, updateRequest);
        // then
        assertThat(toProductUpdated).isNotNull();
        assertThat(toProductUpdated.getId()).isEqualTo(findProduct.getId());
        assertThat(toProductUpdated.getTitle()).isEqualTo(updateRequest.title());
        assertThat(toProductUpdated.getDescription()).isEqualTo(updateRequest.description());
        assertThat(toProductUpdated.getQuantityInStock()).isEqualTo(updateRequest.quantity());
        assertThat(toProductUpdated.getPrice()).isEqualTo(updateRequest.price());
        assertThat(toProductUpdated.getValidityNumberOfDays()).isEqualTo(updateRequest.validityDays());
        assertThat(toProductUpdated.getTitle()).isNotEqualTo(findProduct.getTitle());
        assertThat(toProductUpdated.getDescription()).isNotEqualTo(findProduct.getDescription());
        assertThat(toProductUpdated.getQuantityInStock()).isNotEqualTo(findProduct.getQuantityInStock());
        assertThat(toProductUpdated.getPrice()).isNotEqualTo(findProduct.getPrice());
        assertThat(toProductUpdated.getValidityNumberOfDays()).isNotEqualTo(findProduct.getValidityNumberOfDays());
    }

    @Test
    void itShouldReturnProductResponseFromProduct() {
        // when
        ProductResponse productResponse = mapper.toResponse(product);
        // then
        assertThat(productResponse).isNotNull();
        assertThat(productResponse.id()).isEqualTo(product.getId());
        assertThat(productResponse.title()).isEqualTo(product.getTitle());
        assertThat(productResponse.description()).isEqualTo(product.getDescription());
        assertThat(productResponse.quantity()).isEqualTo(product.getQuantityInStock());
        assertThat(productResponse.price()).isEqualTo(product.getPrice());
        assertThat(productResponse.validityDays()).isEqualTo(product.getValidityNumberOfDays());
    }
}