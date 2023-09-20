package com.github.wesleybritovlk.leasingserviceapi.leasingcart;

import com.github.wesleybritovlk.leasingserviceapi.user.User;

public interface LeasingCartMapper {
    LeasingCart toLeasing(User findUser);

    LeasingCart toLeasing(LeasingCart findLeasingCart, User findUser);

    LeasingCartResponse toResponse(LeasingCart leasingCart);
}