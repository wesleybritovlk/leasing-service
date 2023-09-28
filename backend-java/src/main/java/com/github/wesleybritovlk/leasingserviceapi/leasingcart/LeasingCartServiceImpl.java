package com.github.wesleybritovlk.leasingserviceapi.leasingcart;

import com.github.wesleybritovlk.leasingserviceapi.leasingcart.itemleasing.ItemLeasingRepository;
import com.github.wesleybritovlk.leasingserviceapi.user.User;
import com.github.wesleybritovlk.leasingserviceapi.user.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

import static com.github.wesleybritovlk.leasingserviceapi.leasingcart.LeasingCartPolitic.updateTotalPriceAndExpirationDate;
import static com.github.wesleybritovlk.leasingserviceapi.product.ProductPolitic.returnQuantityToStock;
import static java.lang.System.currentTimeMillis;
import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
@RequiredArgsConstructor
public class LeasingCartServiceImpl implements LeasingCartService {
    private final LeasingCartRepository repository;
    private final LeasingCartMapper mapper;
    private final UserRepository userRepository;
    private final ItemLeasingRepository itemLeasingRepository;

    private static final Logger LOGGER = getLogger(LeasingCartService.class);

    private LeasingCart findLeasing(UUID id) {
        return repository.findById(id).orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Leasing not found, please check the id"));
    }

    private User findUser(UUID id) {
        return userRepository.findById(id).orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "User not found, please check the id"));
    }

    @Transactional
    @Override
    public LeasingCart create(LeasingCartRequest requestCreate) {
        var startTime = currentTimeMillis();
        var leasingCart = mapper.toLeasing(findUser(requestCreate.user()));
        var create = repository.save(leasingCart);
        LOGGER.info("DB Create : Persisted leasing_cart ID: {} in {}ms", create.getId(), currentTimeMillis() - startTime);
        return create;
    }

    @Override
    public LeasingCartResponse findById(UUID id) {
        var startTime = currentTimeMillis();
        var findById = mapper.toResponse(findLeasing(id));
        LOGGER.info("DB FindById : Returned leasing_cart ID: {} in {}ms", findById.id(), currentTimeMillis() - startTime);
        return findById;
    }

    @Override
    public Page<LeasingCartResponse> findAll(Pageable pageable) {
        var startTime = currentTimeMillis();
        var findAll = repository.findAll(pageable).map(mapper::toResponse);
        LOGGER.info("DB FindAll : Returned {} leasing_carts in {}ms", findAll.getTotalElements(), currentTimeMillis() - startTime);
        return findAll;
    }

    @Transactional
    @Override
    public LeasingCart update(UUID id, LeasingCartRequest requestUpdate) {
        var startTime = currentTimeMillis();
        var findLeasingCart = findLeasing(id);
        var findOrUpdateUser = findUser(requestUpdate.user());
        var leasingCart = mapper.toLeasing(findLeasingCart, findOrUpdateUser);
        updateTotalPriceAndExpirationDate(leasingCart);
        var update = repository.save(leasingCart);
        LOGGER.info("DB Update : Updated leasing_cart ID: {} in {}ms", update.getId(), currentTimeMillis() - startTime);
        return update;
    }

    @Transactional
    @Override
    public void delete(UUID id) {
        var startTime = currentTimeMillis();
        var leasingCart = findLeasing(id);
        leasingCart.getItemsLeasing().forEach(item -> {
            var findProduct = item.getProduct();
            returnQuantityToStock(findProduct, item.getQuantityToLeased());

        });
        LOGGER.info("DB DeleteAll : Deleted {} items_leasing in {}ms", leasingCart.getItemsLeasing().size(), currentTimeMillis() - startTime);
        itemLeasingRepository.deleteAll(leasingCart.getItemsLeasing());
        repository.delete(leasingCart);
        LOGGER.info("DB Delete : Deleted leasing_cart ID: {} in {}ms", leasingCart.getId(), currentTimeMillis() - startTime);
    }
}