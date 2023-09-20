package com.github.wesleybritovlk.leasingserviceapi.product;

import com.github.wesleybritovlk.leasingserviceapi.leasingcart.itemleasing.ItemLeasingRequestUpdate;
import com.github.wesleybritovlk.leasingserviceapi.leasingcart.itemleasing.ItemLeasingService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.http.HttpStatus.CONFLICT;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository repository;
    private final ProductMapper mapper;
    private final ItemLeasingService itemLeasingService;

    private static final Logger LOGGER = getLogger(ProductService.class);

    private void invalidateDuplicateProductName(String requestProductName) {
        var findByTitle = repository.findByTitle(requestProductName);
        if (findByTitle.isPresent()) {
            var message = "This Product is already registered";
            throw new ResponseStatusException(CONFLICT, message);
        }
        LOGGER.info("M findProductByTitle, Product={}", findByTitle);
    }

    @Transactional
    @Override
    public Product create(ProductRequest creationRequest) {
        invalidateDuplicateProductName(creationRequest.title());
        var product = mapper.toProduct(creationRequest);
        LOGGER.info("M create, Product={}", product);
        return repository.save(product);
    }

    private Product findProduct(UUID id) {
        var product = repository.findById(id).orElseThrow(() -> {
            var message = "Product not found, please check the id";
            return new ResponseStatusException(HttpStatus.NOT_FOUND, message);
        });
        LOGGER.info("M findProduct, Product={}", product);
        return product;
    }

    public ProductResponse getById(UUID id) {
        var productResponse = mapper.toResponse(findProduct(id));
        LOGGER.info("M getProduct, Product={}", productResponse);
        return productResponse;
    }

    public Page<ProductResponse> getAll(Pageable pageable) {
        return repository.findAll(pageable).map(mapper::toResponse);
    }

    public Page<ProductResponse> getAllByNameOrDescription(String query, Pageable pageable) {
        var replacedQuery = query.replaceAll("(^\\s+)|(\\s+$)", "");
        return repository.searchProductsByNameOrDescription(replacedQuery, replacedQuery, pageable).map(mapper::toResponse);
    }

    @Transactional
    @Override
    public Product update(UUID id, ProductRequest updateRequest) {
        var findProduct = findProduct(id);
        if (!updateRequest.title().equals(findProduct.getTitle()))
            invalidateDuplicateProductName(updateRequest.title());
        var product = mapper.toProduct(findProduct, updateRequest);
        repository.save(product);
        LOGGER.info("M update, Product={}", product);
        product.getItemsLeasing().forEach(itemLeasing -> {
            ItemLeasingRequestUpdate itemLeasingRequestUpdate = new ItemLeasingRequestUpdate(
                    itemLeasing.getQuantityToLeased()
            );
            itemLeasingService.update(itemLeasing.getId(), itemLeasingRequestUpdate);
        });
        LOGGER.info("M updateLeasedProducts, Product={LeasedProducts={}}", product);
        return repository.save(product);
    }

    @Transactional
    @Override
    public void delete(UUID id) {
        var product = findProduct(id);
        product.getItemsLeasing().forEach(itemLeasing -> itemLeasingService.delete(itemLeasing.getId()));
        LOGGER.info("M deleteLeasedProducts, Product={LeasedProducts={}}", product);
        repository.delete(product);
        LOGGER.info("M delete, Product={}", product);
    }
}