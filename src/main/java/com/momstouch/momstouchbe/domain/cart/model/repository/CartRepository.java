package com.momstouch.momstouchbe.domain.cart.model.repository;

import com.momstouch.momstouchbe.domain.cart.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {


}
