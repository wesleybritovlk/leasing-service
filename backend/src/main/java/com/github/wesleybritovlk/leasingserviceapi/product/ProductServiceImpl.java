package com.github.wesleybritovlk.leasingserviceapi.product;

import com.github.wesleybritovlk.leasingserviceapi.leasingcart.itemleasing.ItemLeasingRequestUpdate;
import com.github.wesleybritovlk.leasingserviceapi.leasingcart.itemleasing.ItemLeasingService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

import static java.lang.System.currentTimeMillis;
import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository repository;
    private final ProductMapper mapper;
    private final ItemLeasingService itemLeasingService;

    private static final Logger LOGGER = getLogger(ProductService.class);

    private void invalidateDuplicateProductName(String requestProductTitle) {
        if (repository.existsByTitle(requestProductTitle))
            throw new ResponseStatusException(CONFLICT, "This Product is already registered");
    }

    @Transactional
    @Override
    public Product create(ProductRequest creationRequest) {
        var startTime = currentTimeMillis();
        invalidateDuplicateProductName(creationRequest.title());
        var product = mapper.toProduct(creationRequest);
        var create = repository.save(product);
        LOGGER.info("DB Create : Persisted product ID: {} in {}ms", create.getId(), currentTimeMillis() - startTime);
        return create;
    }

    private Product findProduct(UUID id) {
        return repository.findById(id).orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Product not found, please check the id"));
    }

    public ProductResponse findById(UUID id) {
        var startTime = currentTimeMillis();
        var findById = mapper.toResponse(findProduct(id));
        LOGGER.info("DB FindById : Returned product ID: {} in {}ms", findById.id(), currentTimeMillis() - startTime);
        return findById;
    }

    public Page<ProductResponse> findAll(Pageable pageable) {
        var startTime = currentTimeMillis();
        var findAll = repository.findAll(pageable).map(mapper::toResponse);
        LOGGER.info("DB FindAll : Returned {} products in {}ms", findAll.getTotalElements(), currentTimeMillis() - startTime);
        return findAll;
    }

    public Page<ProductResponse> findAllByNameOrDescription(String query, Pageable pageable) {
        var startTime = currentTimeMillis();
        var replacedQuery = query.replaceAll("(^\\s+)|(\\s+$)", "");
        var findAllByNameOrDescription = repository.searchProductsByNameOrDescription(replacedQuery, replacedQuery, pageable).map(mapper::toResponse);
        LOGGER.info("DB FindAllByNameOrDescription : Returned {} products in {}ms", findAllByNameOrDescription.getTotalElements(), currentTimeMillis() - startTime);
        return findAllByNameOrDescription;
    }

    @Transactional
    @Override
    public Product update(UUID id, ProductRequest updateRequest) {
        var startTime = currentTimeMillis();
        var findProduct = findProduct(id);
        if (!updateRequest.title().equals(findProduct.getTitle()))
            invalidateDuplicateProductName(updateRequest.title());
        var product = mapper.toProduct(findProduct, updateRequest);
        repository.save(product);
        LOGGER.info("DB Update : Updated product ID: {} in {}ms", product.getId(), currentTimeMillis() - startTime);
        product.getItemsLeasing().forEach(itemLeasing -> {
            var itemLeasingRequestUpdate = new ItemLeasingRequestUpdate(itemLeasing.getQuantityToLeased());
            itemLeasingService.update(itemLeasing.getId(), itemLeasingRequestUpdate);
        });
        var updateProductAndItemsLeasing = repository.save(product);
        LOGGER.info("DB Update : Updated {} items_leasing in {}ms", updateProductAndItemsLeasing.getItemsLeasing().size(), currentTimeMillis() - startTime);
        return updateProductAndItemsLeasing;
    }

    @Transactional
    @Override
    public void delete(UUID id) {
        var startTime = currentTimeMillis();
        var product = findProduct(id);
        LOGGER.info("DB DeleteAll : Deleted {} items_leasing in {}ms", product.getItemsLeasing().size(), currentTimeMillis() - startTime);
        product.getItemsLeasing().forEach(itemLeasing -> itemLeasingService.delete(itemLeasing.getId()));
        repository.delete(product);
        LOGGER.info("DB Delete : Deleted product ID: {} in {}ms", product.getId(), currentTimeMillis() - startTime);
    }
}