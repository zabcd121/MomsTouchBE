package com.momstouch.momstouchbe.domain.cart.model.repository;

import com.momstouch.momstouchbe.domain.cart.model.Cart;

import java.util.List;

public interface CartQueryRepository {

    Cart findByMemberId(Long memberId);

    boolean existsByMemberId(Long memberId);

}
