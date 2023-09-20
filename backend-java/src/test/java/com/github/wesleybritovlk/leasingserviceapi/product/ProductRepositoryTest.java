package com.github.wesleybritovlk.leasingserviceapi.product;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static java.math.BigInteger.valueOf;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@AutoConfigureTestEntityManager
class ProductRepositoryTest {
    private final ProductRepository underTest;
    private Product product;
    private Product product2;

    @Autowired
    ProductRepositoryTest(ProductRepository underTest) {
        this.underTest = underTest;
    }

    @BeforeEach
    void setUp() {
        product = Product.builder().title("test1").description("test1").quantityInStock(valueOf(10)).price(BigDecimal.valueOf(10.00)).validityNumberOfDays(10).build();
        product2 = Product.builder().title("test0").description("null").quantityInStock(valueOf(100)).price(BigDecimal.valueOf(100.00)).validityNumberOfDays(30).build();
    }

    @AfterEach
    void tearDown() {
        underTest.deleteAll();
    }

    @Test
    void itShouldSaveProduct() {
        // when
        String expectedName = "test0";
        Product save = underTest.save(product2);
        // then
        assertThat(save.getTitle()).isEqualTo(expectedName);
        assertThat(save.getId()).isNotNull();
    }

    @Test
    void itShouldFindProductById() {
        // given
        underTest.save(product);
        // when
        UUID expectedId = product.getId();
        Optional<Product> expectedProduct = Optional.of(product);
        Optional<Product> findProduct = underTest.findById(expectedId);
        // then
        assertThat(findProduct.get().getId()).isEqualTo(expectedId);
        assertThat(findProduct).isEqualTo(expectedProduct);
        assertThat(findProduct).isNotEqualTo(product);
        assertThat(findProduct).isNotNull();
    }

    @Test
    void itShouldFindAllProductsWithPagination() {
        // given
        underTest.save(product);
        underTest.save(product2);
        Pageable pageable = PageRequest.of(0, 10);
        // when
        Integer expectedPageNumber = 0;
        Integer expectedPageSize = 10;
        Page<Product> pageFindAll = underTest.findAll(pageable);
        // then
        assertThat(pageFindAll).isNotNull();
        assertThat(pageFindAll.getNumber()).isEqualTo(expectedPageNumber);
        assertThat(pageFindAll.getSize()).isEqualTo(expectedPageSize);
        assertThat(pageFindAll.getTotalElements()).isEqualTo(2L);
        assertThat(pageFindAll.getTotalPages()).isEqualTo(1);
        assertThat(pageFindAll.getContent()).isEqualTo(List.of(product, product2));
    }

    @Test
    void itShouldSearchProductsByNameOrDescriptionWithPagination() {
        // given
        underTest.save(product);
        Product onlySavedToBeFound = underTest.save(product2);
        Pageable pageable = PageRequest.of(0, 10);
        // when
        Integer expectedPageNumber = 0;
        Integer expectedPageSize = 10;
        String expectedSearch = "ull";
        Page<Product> pageFindNameDescription = underTest.searchProductsByNameOrDescription(expectedSearch, expectedSearch, pageable);
        // then
        assertThat(pageFindNameDescription).isNotNull();
        assertThat(pageFindNameDescription.getNumber()).isEqualTo(expectedPageNumber);
        assertThat(pageFindNameDescription.getSize()).isEqualTo(expectedPageSize);
        assertThat(pageFindNameDescription.getTotalPages()).isEqualTo(1);
        assertThat(pageFindNameDescription.getTotalElements()).isEqualTo(1L);
        assertThat(pageFindNameDescription.getContent()).isEqualTo(List.of(onlySavedToBeFound));
        assertThat(pageFindNameDescription.get().findFirst().get().getTitle()).containsIgnoringCase(onlySavedToBeFound.getTitle());
        assertThat(pageFindNameDescription.get().findFirst().get().getDescription()).containsIgnoringCase(onlySavedToBeFound.getDescription());
    }

    @Test
    void itShouldUpdateProduct() {
        // given
        Product create = underTest.save(product);
        // when
        String notExpectedName = "test1";
        Product update = underTest.save(Product.builder().id(create.getId()).title("updated Name").description("updated Description").quantityInStock(valueOf(20)).price(BigDecimal.valueOf(5.00)).validityNumberOfDays(10).build());
        // then
        assertThat(update).isNotNull();
        assertThat(update.getId()).isNotNull();
        assertThat(update.getId()).isEqualTo(create.getId());
        assertThat(update.getTitle()).isNotEqualTo(notExpectedName);
    }

    @Test
    void itShouldDeleteProduct() {
        // given
        underTest.save(product);
        // when
        underTest.delete(product);
        Optional<Product> productDeleted = underTest.findById(product.getId());
        // then
        assertThat(productDeleted).isNotPresent();
        assertThat(productDeleted).isNotEqualTo(product);
    }
}