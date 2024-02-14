package com.github.wesleybritovlk.leasingserviceapi.leasingcart.itemleasing;

import com.github.wesleybritovlk.leasingserviceapi.leasingcart.LeasingCart;
import com.github.wesleybritovlk.leasingserviceapi.product.Product;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.ZonedDateTime;

import static java.math.BigDecimal.valueOf;
import static java.time.ZoneId.of;
import static java.time.ZonedDateTime.now;

@Component
class ItemLeasingMapperImpl implements ItemLeasingMapper {
    private static BigDecimal getPriceByQuantity(BigDecimal findProduct, BigInteger request) {
        var multiplicand = valueOf(request.longValue());
        return findProduct.multiply(multiplicand);
    }

    private static ZonedDateTime getExpirationDateWithPlusDays(Integer findProduct) {
        return now(of("America/Sao_Paulo")).plusDays(findProduct);
    }

    @Override
    public ItemLeasing toItemLeasing(ItemLeasingRequestCreate creationRequest, LeasingCart findLeasingCart, Product findProduct) {
        return ItemLeasing.builder().quantityToLeased(creationRequest.quantity()).subtotalPrice(getPriceByQuantity(findProduct.getPrice(), creationRequest.quantity())).expirationDate(getExpirationDateWithPlusDays(findProduct.getValidityNumberOfDays())).leasingCart(findLeasingCart).product(findProduct).createdAt(now(of("America/Sao_Paulo"))).updatedAt(now(of("America/Sao_Paulo"))).build();
    }

    private static ZonedDateTime getExpirationDateWithPlusDays(ItemLeasing findItemLeasing, Product findProduct) {
        Integer leasedProductValidityNumberOfDays = findItemLeasing.getProduct().getValidityNumberOfDays(), productValidityNumberOfDays = findProduct.getValidityNumberOfDays();
        return !leasedProductValidityNumberOfDays.equals(productValidityNumberOfDays) ? now(of("America/Sao_Paulo")).plusDays(findProduct.getValidityNumberOfDays()) : findItemLeasing.getExpirationDate();
    }

    @Override
    public ItemLeasing toItemLeasing(ItemLeasing findItemLeasing, ItemLeasingRequestUpdate requestUpdate, LeasingCart findLeasingCart, Product findProduct) {
        return ItemLeasing.builder().id(findItemLeasing.getId()).quantityToLeased(requestUpdate.quantity()).subtotalPrice(getPriceByQuantity(findProduct.getPrice(), requestUpdate.quantity())).expirationDate(getExpirationDateWithPlusDays(findItemLeasing, findProduct)).leasingCart(findLeasingCart).product(findProduct).createdAt(findItemLeasing.getCreatedAt()).updatedAt(now(of("America/Sao_Paulo"))).build();
    }

    @Override
    public ItemLeasingResponse toResponse(ItemLeasing itemLeasing) {
        return new ItemLeasingResponse(itemLeasing.getId(), itemLeasing.getLeasingCart().getId(), itemLeasing.getProduct().getId(), itemLeasing.getProduct().getTitle(), itemLeasing.getQuantityToLeased(), itemLeasing.getSubtotalPrice(), itemLeasing.getExpirationDate(), itemLeasing.getProduct().getImagesUrl().get(0));
    }
}