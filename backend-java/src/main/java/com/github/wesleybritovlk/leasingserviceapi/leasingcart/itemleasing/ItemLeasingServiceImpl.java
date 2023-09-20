package com.github.wesleybritovlk.leasingserviceapi.leasingcart.itemleasing;

import com.github.wesleybritovlk.leasingserviceapi.leasingcart.LeasingCartRepository;
import com.github.wesleybritovlk.leasingserviceapi.product.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

import static com.github.wesleybritovlk.leasingserviceapi.leasingcart.LeasingCartPolitic.updateTotalPriceAndExpirationDate;
import static com.github.wesleybritovlk.leasingserviceapi.product.ProductPolitic.*;
import static java.lang.System.currentTimeMillis;
import static java.math.BigDecimal.ZERO;
import static org.slf4j.LoggerFactory.*;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
@Transactional
@RequiredArgsConstructor
public class ItemLeasingServiceImpl implements ItemLeasingService {
    private final ItemLeasingRepository repository;
    private final ItemLeasingMapper mapper;
    private final LeasingCartRepository leasingCartRepository;
    private final ProductRepository productRepository;

    private static final Logger LOGGER = getLogger(ItemLeasingService.class);

    private ItemLeasing findItemLeasing(UUID id) {
        return repository.findById(id).orElseThrow(() -> {
            var message = "Leased title not found, please check the id";
            return new ResponseStatusException(NOT_FOUND, message);
        });
    }

    @Override
    public ItemLeasing create(ItemLeasingRequestCreate requestCreate) {
        var startTime = currentTimeMillis();
        var findLeasingCart = leasingCartRepository.findById(requestCreate.cart()).orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Leasing not found, please check the id"));
        var findProduct = productRepository.findById(requestCreate.product()).orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Product not found, please check the id"));
        var itemLeasing = mapper.toItemLeasing(requestCreate, findLeasingCart, findProduct);
        findLeasingCart.getItemsLeasing().add(itemLeasing);
        LOGGER.info("DB Create : Persisted item_leasing ID: {} in {}ms", itemLeasing.getId(), currentTimeMillis() - startTime);
        retireQuantityInStock(itemLeasing.getProduct(), requestCreate.quantity());
        updateTotalPriceAndExpirationDate(itemLeasing.getLeasingCart());
        var createItemAndUpdateCart = repository.save(itemLeasing);
        LOGGER.info("DB Update : Updated leasing_cart ID: {} in {}ms", itemLeasing.getLeasingCart().getId(), currentTimeMillis() - startTime);
        return createItemAndUpdateCart;
    }

    @Override
    public ItemLeasingResponse findById(UUID id) {
        var startTime = currentTimeMillis();
        var findById = mapper.toResponse(findItemLeasing(id));
        LOGGER.info("DB FindById : Returned item_leasing ID: {} in {}ms", findById.id(), currentTimeMillis() - startTime);
        return findById;
    }

    @Override
    public Page<ItemLeasingResponse> findAll(Pageable pageable) {
        var startTime = currentTimeMillis();
        var findAll = repository.findAll(pageable).map(mapper::toResponse);
        LOGGER.info("DB FindAll : Returned {} items_leasing in {}ms", findAll.getTotalElements(), currentTimeMillis() - startTime);
        return findAll;
    }

    @Override
    public ItemLeasing update(UUID id, ItemLeasingRequestUpdate requestUpdate) {
        var startTime = currentTimeMillis();
        var findItemLeasing = findItemLeasing(id);
        var product = findItemLeasing.getProduct();
        var leasingCart = findItemLeasing.getLeasingCart();
        var itemLeasing = mapper.toItemLeasing(findItemLeasing, requestUpdate, leasingCart, product);
        updateQuantityInStock(product, findItemLeasing.getQuantityToLeased(), requestUpdate.quantity());
        repository.save(itemLeasing);
        LOGGER.info("DB Update : Updated item_leasing ID: {} in {}ms", itemLeasing.getId(), currentTimeMillis() - startTime);
        updateTotalPriceAndExpirationDate(leasingCart);
        var updateItemAndUpdateCart = repository.save(itemLeasing);
        LOGGER.info("DB Update : Updated leasing_cart ID: {} in {}ms", updateItemAndUpdateCart.getLeasingCart().getId(), currentTimeMillis() - startTime);
        return updateItemAndUpdateCart;
    }

    @Override
    public void delete(UUID id) {
        var startTime = currentTimeMillis();
        var itemLeasing = findItemLeasing(id);
        var product = itemLeasing.getProduct();
        var leasingCart = itemLeasing.getLeasingCart();
        returnQuantityToStock(product, itemLeasing.getQuantityToLeased());
        itemLeasing.setSubtotalPrice(ZERO);
        repository.save(itemLeasing);
        LOGGER.info("DB Update : Updated item_leasing ID: {} in {}ms", itemLeasing.getId(), currentTimeMillis() - startTime);
        leasingCart.getItemsLeasing().remove(itemLeasing);
        updateTotalPriceAndExpirationDate(leasingCart);
        LOGGER.info("DB Update : Updated leasing_cart ID: {} in {}ms", leasingCart.getId(), currentTimeMillis() - startTime);
        repository.delete(itemLeasing);
        LOGGER.info("DB Delete : Deleted item_leasing ID: {} in {}ms", itemLeasing.getId(), currentTimeMillis() - startTime);
    }
}