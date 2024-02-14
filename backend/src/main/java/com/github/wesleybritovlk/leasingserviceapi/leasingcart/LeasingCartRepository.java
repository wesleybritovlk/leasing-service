package com.github.wesleybritovlk.leasingserviceapi.leasingcart;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface LeasingCartRepository extends JpaRepository<LeasingCart, UUID> {
}