package com.github.wesleybritovlk.leasingserviceapi.leasingcart;

import com.github.wesleybritovlk.leasingserviceapi.leasingcart.itemleasing.ItemLeasing;
import com.github.wesleybritovlk.leasingserviceapi.leasingcart.itemleasing.ItemLeasingMapper;
import com.github.wesleybritovlk.leasingserviceapi.leasingcart.itemleasing.ItemLeasingResponse;
import com.github.wesleybritovlk.leasingserviceapi.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.TreeSet;

import static java.math.BigDecimal.ZERO;
import static java.time.ZoneId.of;
import static java.time.ZonedDateTime.now;
import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toSet;

@Component
@RequiredArgsConstructor
public class LeasingCartMapperImpl implements LeasingCartMapper {
    private final ItemLeasingMapper itemLeasingMapper;

    @Override
    public LeasingCart toLeasing(User findUser) {
        var zoneId = of("America/Sao_Paulo");
        var midnight = now(zoneId).toLocalDate().atStartOfDay(zoneId);
        Set<ItemLeasing> itemsLeasing = new TreeSet<>(comparing(item -> item.getProduct().getTitle()));
        return LeasingCart.builder().totalPrice(ZERO).firstExpirationDate(midnight).lastExpirationDate(midnight).itemsLeasing(itemsLeasing).user(findUser).createdAt(now(of("America/Sao_Paulo"))).updatedAt(now(of("America/Sao_Paulo"))).build();
    }

    @Override
    public LeasingCart toLeasing(LeasingCart findLeasingCart, User findUser) {
        return LeasingCart.builder().id(findLeasingCart.getId()).totalPrice(findLeasingCart.getTotalPrice()).firstExpirationDate(findLeasingCart.getFirstExpirationDate()).lastExpirationDate(findLeasingCart.getLastExpirationDate()).itemsLeasing(findLeasingCart.getItemsLeasing()).user(findUser).createdAt(findLeasingCart.getCreatedAt()).updatedAt(now(of("America/Sao_Paulo"))).build();
    }

    private Set<ItemLeasingResponse> getLeasedProductToResponse(Set<ItemLeasing> itemsLeasing) {
        return itemsLeasing.stream().map(itemLeasingMapper::toResponse).collect(toSet());
    }

    @Override
    public LeasingCartResponse toResponse(LeasingCart leasingCart) {
        return new LeasingCartResponse(leasingCart.getId(), leasingCart.getUser().getId(), leasingCart.getUser().getFullName(), leasingCart.getTotalPrice(), leasingCart.getFirstExpirationDate(), leasingCart.getLastExpirationDate(), getLeasedProductToResponse(leasingCart.getItemsLeasing()));
    }
}