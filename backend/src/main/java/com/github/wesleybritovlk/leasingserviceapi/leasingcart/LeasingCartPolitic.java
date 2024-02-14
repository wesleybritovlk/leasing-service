package com.github.wesleybritovlk.leasingserviceapi.leasingcart;

import com.github.wesleybritovlk.leasingserviceapi.leasingcart.itemleasing.ItemLeasing;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

import static java.math.BigDecimal.ZERO;
import static java.time.ZoneId.of;
import static java.time.ZonedDateTime.now;
import static java.util.Comparator.naturalOrder;
import static java.util.concurrent.CompletableFuture.supplyAsync;

public class LeasingCartPolitic {
    private static ZonedDateTime getMidnight() {
        var zoneId = of("America/Sao_Paulo");
        return now(zoneId).toLocalDate().atStartOfDay(zoneId);
    }

    public static void updateTotalPriceAndExpirationDate(LeasingCart findLeasingCart) {
        var leasedProducts = findLeasingCart.getItemsLeasing();
        var midnight = getMidnight();
        var totalPrice = supplyAsync(() -> leasedProducts.stream().map(ItemLeasing::getSubtotalPrice).reduce(BigDecimal::add).orElse(ZERO));
        var lastExpirationDate = supplyAsync(() -> leasedProducts.stream().map(ItemLeasing::getExpirationDate).max(naturalOrder()).orElse(midnight));
        var firstExpirationDate = supplyAsync(() -> leasedProducts.stream().map(ItemLeasing::getExpirationDate).min(naturalOrder()).orElse(midnight));
        findLeasingCart.setTotalPrice(totalPrice.join());
        findLeasingCart.setLastExpirationDate(lastExpirationDate.join());
        findLeasingCart.setFirstExpirationDate(firstExpirationDate.join());
    }
}