package com.github.wesleybritovlk.leasingserviceapi.product;

public interface ProductMapper {
    Product toProduct(ProductRequest creationRequest);
    Product toProduct(Product findProduct, ProductRequest updateRequest);
    ProductResponse toResponse(Product product);
}
