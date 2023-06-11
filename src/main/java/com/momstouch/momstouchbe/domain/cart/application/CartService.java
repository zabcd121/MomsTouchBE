package com.momstouch.momstouchbe.domain.cart.application;

import com.momstouch.momstouchbe.domain.cart.dto.CartRequest;
import com.momstouch.momstouchbe.domain.cart.model.Cart;
import com.momstouch.momstouchbe.domain.cart.model.CartMenu;
import com.momstouch.momstouchbe.domain.cart.model.CartMenuOption;
import com.momstouch.momstouchbe.domain.cart.model.CartMenuOptionGroup;
import com.momstouch.momstouchbe.domain.discountpolicy.model.repository.DiscountPolicyRepository;
import com.momstouch.momstouchbe.domain.member.model.repository.MemberRepository;
import com.momstouch.momstouchbe.global.domain.Money;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

import static com.momstouch.momstouchbe.domain.cart.dto.CartRequest.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CartService {

    private final MemberRepository memberRepository;
    private final DiscountPolicyRepository discountPolicyRepository;

    public void addCartMenu(Long memberId, CartMenuAddRequest cartMenuAddRequest) {
        Cart cart = Cart.builder()
                .member(memberRepository.getReferenceById(memberId))
                .build();

        cart.addCartMenu(
                CartMenu.builder()
                        .menuId(cartMenuAddRequest.getMenuId())
                        .discountPolicy(discountPolicyRepository.getReferenceById(cartMenuAddRequest.getDiscountPolicyId()))
                        .quantity(cartMenuAddRequest.getQuantity())
                        .price(Money.of(cartMenuAddRequest.getPrice()))
                        .cartMenuOptionGroupList(
                                cartMenuAddRequest.getCartMenuOptionGroupList().stream()
                                        .map(cartMenuOptionGroupRequest ->
                                                CartMenuOptionGroup.builder()
                                                .menuOptionGroupId(cartMenuOptionGroupRequest.getMenuOptionGroupId())
                                                .cartMenuOptionList(
                                                        cartMenuOptionGroupRequest.getCartMenuOptionList().stream()
                                                                .map(cartMenuOptionRequest ->
                                                                        CartMenuOption.builder()
                                                                        .menuOptionId(cartMenuOptionRequest.getMenuOptionId())
                                                                        .price(Money.of(cartMenuOptionRequest.getPrice()))
                                                                        .build())
                                                                .collect(Collectors.toList())
                                                )
                                                .build())
                                        .collect(Collectors.toList())
                        ).build()
        );

    }
}
