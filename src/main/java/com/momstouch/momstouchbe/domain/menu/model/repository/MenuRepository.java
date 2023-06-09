package com.momstouch.momstouchbe.domain.menu.model.repository;

import com.momstouch.momstouchbe.domain.menu.model.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Long> {
}
