package com.momstouch.momstouchbe.domain.shop.model.repository;

import com.momstouch.momstouchbe.domain.shop.model.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MenuRepository extends JpaRepository<Menu,Long> {

    @Query
    boolean existsByName(String name);
}
