package com.momstouch.momstouchbe.domain.shop.model.repository;

import com.momstouch.momstouchbe.domain.shop.model.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MenuRepository extends JpaRepository<Menu,Long> {

    @Query
    boolean existsByName(String name);

    @Query("select m from Menu m where m.discountPolicy.id = :discountPolicyId")
    List<Menu> findByDiscountPolicyId(Long discountPolicyId);

    @Modifying
    @Query("update Menu m set m.discountPolicy.id = :setId where m.discountPolicy.id = :discountPolicyId")
    int updateMenuDiscountPolicy(@Param("setId") Long setId, @Param("discountPolicyId") Long discountPolicyId);
}
