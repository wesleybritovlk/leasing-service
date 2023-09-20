package com.github.wesleybritovlk.leasingserviceapi.product;

import com.github.wesleybritovlk.leasingserviceapi.leasingcart.itemleasing.ItemLeasing;
import com.github.wesleybritovlk.leasingserviceapi.leasingcart.itemleasing.ItemLeasingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static java.math.BigInteger.ONE;
import static java.math.BigInteger.valueOf;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
    private ProductService service;
    @Mock
    private ProductRepository repository;
    @Mock
    private ProductMapper mapper;
    @Mock
    private ItemLeasingService itemLeasingService;
    private Product product;
    private Product product2;

    @BeforeEach
    void setUp() {
        service = new ProductServiceImpl(repository, mapper, itemLeasingService);
        product = Product.builder().id(UUID.randomUUID()).title("Product Test").description("Product Description").quantityInStock(valueOf(10)).price(BigDecimal.valueOf(10.0)).validityNumberOfDays(10).itemsLeasing(Set.of(ItemLeasing.builder().id(UUID.randomUUID()).quantityToLeased(ONE).subtotalPrice(BigDecimal.valueOf(10.0)).expirationDate(ZonedDateTime.now()).product(product).build())).build();
        product2 = Product.builder().id(product.getId()).title("Product Test 1").description("Product Description 1").quantityInStock(valueOf(10)).price(BigDecimal.valueOf(10.0)).validityNumberOfDays(10).itemsLeasing(Set.of(ItemLeasing.builder().id(UUID.randomUUID()).quantityToLeased(ONE).subtotalPrice(BigDecimal.valueOf(10.0)).expirationDate(ZonedDateTime.now()).product(product).build())).build();
    }

    private static ProductRequest getProductRequest(Product product) {
        return new ProductRequest(product.getTitle(), product.getDescription(), product.getQuantityInStock(), product.getPrice(), product.getValidityNumberOfDays(), product.getImagesUrl());
    }

    private static ProductResponse getProductResponse(Product product) {
        return new ProductResponse(product.getId(), product.getTitle(), product.getDescription(), product.getQuantityInStock(), product.getPrice(), product.getValidityNumberOfDays(), product.getImagesUrl());
    }

    @Test
    void itShouldCreateProductByProductRequest() {
        // given
        ProductRequest productRequest = getProductRequest(product);
        // when
        when(mapper.toProduct(any(ProductRequest.class))).thenReturn(product);
        when(repository.save(any(Product.class))).thenReturn(product);
        Product createdProduct = service.create(productRequest);
        // then
        verify(mapper, times(1)).toProduct(any(ProductRequest.class));
        verify(repository, times(1)).save(any(Product.class));
        assertThat(createdProduct).isNotNull();
        assertThat(createdProduct.getId()).isNotNull();
        assertThat(createdProduct.getTitle()).isEqualTo(productRequest.title());
        assertThat(createdProduct.getDescription()).isEqualTo(productRequest.description());
        assertThat(createdProduct.getQuantityInStock()).isEqualTo(productRequest.quantity());
        assertThat(createdProduct.getPrice()).isEqualTo(productRequest.price());
        assertThat(createdProduct.getValidityNumberOfDays()).isEqualTo(productRequest.validityDays());
    }

    @Test
    void itShouldGetProductResponseById() {
        // given
        ProductResponse productResponse = getProductResponse(product);
        // when
        UUID expectedId = product.getId();
        when(repository.findById(expectedId)).thenReturn(Optional.ofNullable(product));
        when(mapper.toResponse(any(Product.class))).thenReturn(productResponse);
        ProductResponse findProduct = service.getById(expectedId);
        // then
        verify(repository, times(1)).findById(expectedId);
        verify(mapper, times(1)).toResponse(any(Product.class));
        assertThat(findProduct).isNotNull();
        assertThat(findProduct.id()).isEqualTo(expectedId);
        assertThat(findProduct.title()).isEqualTo(product.getTitle());
        assertThat(findProduct.description()).isEqualTo(product.getDescription());
        assertThat(findProduct.quantity()).isEqualTo(product.getQuantityInStock());
        assertThat(findProduct.price()).isEqualTo(product.getPrice());
        assertThat(findProduct.validityDays()).isEqualTo(product.getValidityNumberOfDays());
        assertThat(findProduct.id()).isNotEqualTo(UUID.randomUUID());
        assertThatThrownBy(() -> service.getById(UUID.randomUUID())).isInstanceOf(ResponseStatusException.class).hasMessageContaining("Product not found, please check the id");
    }

    @Test
    void itShouldGetAllProductsResponseWithPagination() {
        // given
        ProductResponse productResponse = getProductResponse(product);
        ProductResponse productResponse2 = getProductResponse(product2);
        List<Product> products = List.of(product, product2);
        Pageable pageable = PageRequest.of(0, 10);
        Page<Product> productPage = new PageImpl<>(products, pageable, products.size());
        // when
        when(repository.findAll(pageable)).thenReturn(productPage);
        when(mapper.toResponse(any(Product.class))).thenReturn(productResponse).thenReturn(productResponse2);
        Page<ProductResponse> productResponses = service.getAll(pageable);
        // then
        verify(repository, times(1)).findAll(pageable);
        verify(mapper, times(2)).toResponse(any(Product.class));
        assertThat(productResponses).isNotNull();
        assertThat(productResponses.getTotalElements()).isEqualTo(products.size());
        assertThat(productResponses.getTotalPages()).isEqualTo(1);
        assertThat(productResponses.getContent().get(0)).isNotNull();
        assertThat(productResponses.getContent().get(0).id()).isEqualTo(product.getId());
        assertThat(productResponses.getContent().get(1)).isNotNull();
        assertThat(productResponses.getContent().get(1).id()).isEqualTo(product2.getId());
    }

    @Test
    void itShouldGetAllProductsResponseByNameOrDescriptionWithPagination() {
        // given
        ProductResponse productResponse = getProductResponse(product);
        List<Product> products = List.of(product);
        Pageable pageable = PageRequest.of(0, 10);
        Page<Product> productPage = new PageImpl<>(products, pageable, products.size());
        // when
        String expectedSearch = "es";
        when(repository.searchProductsByNameOrDescription(expectedSearch, expectedSearch, pageable)).thenReturn(productPage);
        when(mapper.toResponse(any(Product.class))).thenReturn(productResponse);
        Page<ProductResponse> productResponses = service.getAllByNameOrDescription(expectedSearch, pageable);
        // then
        verify(repository, times(1)).searchProductsByNameOrDescription(expectedSearch, expectedSearch, pageable);
        verify(mapper, times(1)).toResponse(any(Product.class));
        assertThat(productResponses).isNotNull();
        assertThat(productResponses.getTotalElements()).isEqualTo(products.size());
        assertThat(productResponses.getTotalPages()).isEqualTo(1);
        assertThat(productResponses.getContent().get(0)).isNotNull();
        assertThat(productResponses.getContent().get(0).id()).isEqualTo(product.getId());
        assertThat(productResponses.getContent().get(0).title()).isEqualTo(product.getTitle());
        assertThat(productResponses.getContent().get(0).description()).isEqualTo(product.getDescription());

    }

    @Test
    void itShouldUpdateProductByIdAndProductRequest() {
        // given
        Product productToUpdate = product2;
        ProductRequest productRequest = getProductRequest(productToUpdate);
        // when
        UUID expectedId = product.getId();
        when(repository.findById(expectedId)).thenReturn(Optional.ofNullable(product));
        when(mapper.toProduct(any(Product.class), any(ProductRequest.class))).thenReturn(productToUpdate);
        when(repository.save(any(Product.class))).thenReturn(productToUpdate);
        Product updateProduct = service.update(expectedId, productRequest);
        // then
        verify(repository, times(1)).findById(expectedId);
        verify(mapper, times(1)).toProduct(any(Product.class), any(ProductRequest.class));
        verify(repository, times(2)).save(any(Product.class));
        assertThat(updateProduct).isNotNull();
        assertThat(updateProduct.getId()).isEqualTo(expectedId);
        assertThat(updateProduct.getTitle()).isEqualTo(productRequest.title());
        assertThat(updateProduct.getDescription()).isEqualTo(productRequest.description());
        assertThat(updateProduct.getQuantityInStock()).isEqualTo(productRequest.quantity());
        assertThat(updateProduct.getPrice()).isEqualTo(productRequest.price());
        assertThat(updateProduct.getId()).isNotEqualTo(UUID.randomUUID());
        assertThat(updateProduct.getValidityNumberOfDays()).isEqualTo(productRequest.validityDays());
        assertThatThrownBy(() -> service.update(UUID.randomUUID(), productRequest)).isInstanceOf(ResponseStatusException.class).hasMessageContaining("Product not found, please check the id");
    }

    @Test
    void itShouldDeleteProductById() {
        // given
        Product productToBeDeleted = product;
        // when
        UUID expectedId = productToBeDeleted.getId();
        when(repository.findById(expectedId)).thenReturn(Optional.of(productToBeDeleted));
        service.delete(expectedId);
        // then
        verify(repository, times(1)).findById(expectedId);
        verify(repository, times(1)).delete(any(Product.class));
        assertThatThrownBy(() -> service.delete(UUID.randomUUID())).isInstanceOf(ResponseStatusException.class).hasMessageContaining("Product not found, please check the id");
    }
}