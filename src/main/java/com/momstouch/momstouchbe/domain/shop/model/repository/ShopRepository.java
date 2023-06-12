package com.momstouch.momstouchbe.domain.shop.model.repository;

import com.momstouch.momstouchbe.domain.shop.model.Shop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ShopRepository extends JpaRepository<Shop, Long> {

    @Query(nativeQuery = true, value = "select * from shop s join menu m on s.shop_id = m.shop_id where m.menu_id = :menuId")
    Shop findShopByMenuId(@Param("menuId") Long menuId);
}
