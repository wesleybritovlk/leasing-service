package com.github.wesleybritovlk.leasingserviceapi.leasingcart.itemleasing;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ItemLeasingRepository extends JpaRepository<ItemLeasing, UUID> {
}