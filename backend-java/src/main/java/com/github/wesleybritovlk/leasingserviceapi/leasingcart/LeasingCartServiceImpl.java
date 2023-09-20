package com.github.wesleybritovlk.leasingserviceapi.leasingcart;

import com.github.wesleybritovlk.leasingserviceapi.leasingcart.itemleasing.ItemLeasingRepository;
import com.github.wesleybritovlk.leasingserviceapi.user.User;
import com.github.wesleybritovlk.leasingserviceapi.user.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

import static com.github.wesleybritovlk.leasingserviceapi.leasingcart.LeasingCartPolitic.updateTotalPriceAndExpirationDate;
import static com.github.wesleybritovlk.leasingserviceapi.product.ProductPolitic.returnQuantityToStock;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
@RequiredArgsConstructor
public class LeasingCartServiceImpl implements LeasingCartService {
    private final LeasingCartRepository repository;
    private final LeasingCartMapper mapper;
    private final UserRepository userRepository;
    private final ItemLeasingRepository itemLeasingRepository;

    private static final Logger LOG = LoggerFactory.getLogger(LeasingCartService.class);

    private LeasingCart findLeasing(UUID id) {
        var leasingCart = repository.findById(id).orElseThrow(() -> {
            var message = "Leasing not found, please check the id";
            return new ResponseStatusException(NOT_FOUND, message);
        });
        LOG.info("M findLeasing, Leasing={}", leasingCart);
        return leasingCart;
    }

    private User findUser(UUID id) {
        var user = userRepository.findById(id).orElseThrow(() -> {
            var message = "User not found, please check the id";
            return new ResponseStatusException(NOT_FOUND, message);
        });
        LOG.info("M findUser, User={}", user);
        return user;
    }

    @Transactional
    @Override
    public LeasingCart create(LeasingCartRequest requestCreate) {
        var leasingCart = mapper.toLeasing(findUser(requestCreate.user()));
        LOG.info("M create, Leasing={}", leasingCart);
        return repository.save(leasingCart);
    }

    @Override
    public LeasingCartResponse findById(UUID id) {
        var leasingCartResponse = mapper.toResponse(findLeasing(id));
        LOG.info("M getLeasing, Leasing={}", leasingCartResponse);
        return leasingCartResponse;
    }

    @Override
    public Page<LeasingCartResponse> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(mapper::toResponse);
    }

    @Transactional
    @Override
    public LeasingCart update(UUID id, LeasingCartRequest requestUpdate) {
        var findLeasingCart = findLeasing(id);
        var findOrUpdateUser = findUser(requestUpdate.user());
        var leasingCart = mapper.toLeasing(findLeasingCart, findOrUpdateUser);
        updateTotalPriceAndExpirationDate(leasingCart);
        LOG.info("M update, Leasing={}", leasingCart);
        return repository.save(leasingCart);
    }

    @Transactional
    @Override
    public void delete(UUID id) {
        var findLeasingCart = findLeasing(id);
        findLeasingCart.getItemsLeasing().forEach(item -> {
            var findProduct = item.getProduct();
            returnQuantityToStock(findProduct, item.getQuantityToLeased());
        });
        itemLeasingRepository.deleteAll(findLeasingCart.getItemsLeasing());
        LOG.info("M deleteLeasedProducts, Leasing={LeasedProducts={}}", findLeasingCart);
        repository.delete(findLeasingCart);
        LOG.info("M delete, Leasing={}", findLeasingCart);
    }
}