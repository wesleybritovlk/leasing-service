package com.github.wesleybritovlk.leasingserviceapi.product;

import com.github.wesleybritovlk.leasingserviceapi.leasingcart.itemleasing.ItemLeasing;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.TreeSet;

import static java.time.ZoneId.of;
import static java.time.ZonedDateTime.now;
import static java.util.Comparator.comparing;

@Service
public class ProductMapperImpl implements ProductMapper {
    @Override
    public Product toProduct(ProductRequest creationRequest) {
        return Product.builder().title(creationRequest.title()).description(creationRequest.description()).quantityInStock(creationRequest.quantity()).price(creationRequest.price()).validityNumberOfDays(creationRequest.validityDays()).imagesUrl(new ArrayList<>(creationRequest.images())).itemsLeasing(new TreeSet<>(comparing(ItemLeasing::getCreatedAt))).createdAt(now(of("America/Sao_Paulo"))).updatedAt(now(of("America/Sao_Paulo"))).build();
    }

    @Override
    public Product toProduct(Product findProduct, ProductRequest updateRequest) {
        return Product.builder().id(findProduct.getId()).title(updateRequest.title()).description(updateRequest.description()).quantityInStock(updateRequest.quantity()).price(updateRequest.price()).validityNumberOfDays(updateRequest.validityDays()).imagesUrl(updateRequest.images()).itemsLeasing(findProduct.getItemsLeasing()).createdAt(findProduct.getCreatedAt()).updatedAt(now(of("America/Sao_Paulo"))).build();
    }

    @Override
    public ProductResponse toResponse(Product product) {
        return new ProductResponse(product.getId(), product.getTitle(), product.getDescription(), product.getQuantityInStock(), product.getPrice(), product.getValidityNumberOfDays(), product.getImagesUrl());
    }
}