package com.github.wesleybritovlk.leasingserviceapi.leasingcart.itemleasing;

import com.github.wesleybritovlk.leasingserviceapi.leasingcart.LeasingCart;
import com.github.wesleybritovlk.leasingserviceapi.product.Product;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.ZonedDateTime;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class ItemLeasingTest {

    @Test
    void itShouldBeEqualsWhenLeasingItemDoesNotCompareLeasingCartAndProduct() {
        // given
        UUID id = UUID.randomUUID();
        ZonedDateTime zonedDateTime = ZonedDateTime.now();
        // when
        ItemLeasing item1 = new ItemLeasing(id, BigInteger.ONE, BigDecimal.TEN, zonedDateTime, LeasingCart.builder().build(), Product.builder().build(), zonedDateTime, zonedDateTime);
        ItemLeasing item2 = new ItemLeasing(id, BigInteger.ONE, BigDecimal.TEN, zonedDateTime, null, null, zonedDateTime, zonedDateTime);
        // then
        assert(item1.equals(item2));
    }

    @Test
    void itShouldBeEqualsWhenLeasingItemHashCodeDoesNotHaveLeasingCartAndProduct() {
        // given
        UUID id = UUID.randomUUID();
        ZonedDateTime zonedDateTime = ZonedDateTime.now();
        // when
        ItemLeasing item1 = new ItemLeasing(id, BigInteger.ONE, BigDecimal.TEN, zonedDateTime, LeasingCart.builder().build(), Product.builder().build(), zonedDateTime, zonedDateTime);
        ItemLeasing item2 = new ItemLeasing(id, BigInteger.ONE, BigDecimal.TEN, zonedDateTime, null, null, zonedDateTime, zonedDateTime);
        // then
        assertThat(item1.hashCode()).isEqualTo(item2.hashCode());
    }
}