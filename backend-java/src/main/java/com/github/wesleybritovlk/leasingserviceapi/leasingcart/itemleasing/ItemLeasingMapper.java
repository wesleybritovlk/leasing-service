package com.github.wesleybritovlk.leasingserviceapi.leasingcart.itemleasing;

import com.github.wesleybritovlk.leasingserviceapi.leasingcart.LeasingCart;
import com.github.wesleybritovlk.leasingserviceapi.product.Product;

public interface ItemLeasingMapper {
    ItemLeasing toItemLeasing(ItemLeasingRequestCreate creationRequest,
                              LeasingCart findLeasingCart,
                              Product findProduct);

    ItemLeasing toItemLeasing(ItemLeasing findItemLeasing,
                              ItemLeasingRequestUpdate requestUpdate,
                              LeasingCart findLeasingCart,
                              Product findProduct);

    ItemLeasingResponse toResponse(ItemLeasing itemLeasing);
}