package com.momstouch.momstouchbe.domain.shop.model.repository;

import com.momstouch.momstouchbe.domain.shop.model.Shop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShopRepository extends JpaRepository<Shop, Long> {
}
