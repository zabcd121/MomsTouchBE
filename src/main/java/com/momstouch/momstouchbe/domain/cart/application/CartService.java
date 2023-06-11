package com.momstouch.momstouchbe.domain.cart.application;

import com.momstouch.momstouchbe.domain.cart.dto.CartResponse;
import com.momstouch.momstouchbe.domain.cart.model.Cart;
import com.momstouch.momstouchbe.domain.cart.model.CartMenu;
import com.momstouch.momstouchbe.domain.cart.model.CartMenuOption;
import com.momstouch.momstouchbe.domain.cart.model.CartMenuOptionGroup;
import com.momstouch.momstouchbe.domain.cart.model.repository.CartQueryRepository;
import com.momstouch.momstouchbe.domain.cart.model.repository.CartRepository;
import com.momstouch.momstouchbe.domain.discountpolicy.model.repository.DiscountPolicyRepository;
import com.momstouch.momstouchbe.domain.member.repository.MemberRepository;
import com.momstouch.momstouchbe.domain.shop.model.Menu;
import com.momstouch.momstouchbe.domain.shop.model.repository.MenuRepository;
import com.momstouch.momstouchbe.global.domain.Money;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

import static com.momstouch.momstouchbe.domain.cart.dto.CartRequest.*;
import static com.momstouch.momstouchbe.domain.cart.dto.CartResponse.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CartService {

    private final MemberRepository memberRepository;
    private final DiscountPolicyRepository discountPolicyRepository;
    private final CartRepository cartRepository;
    private final CartQueryRepository cartQueryRepository;

    @Transactional
    public void addCartMenu(Long memberId, CartMenuAddRequest cartMenuAddRequest) {
        Cart cart = null;
        if(!cartQueryRepository.existsByMemberId(memberId)) {
             cart = Cart.builder()
                    .member(memberRepository.getReferenceById(memberId))
                    .build();

        } else {
            cart = cartQueryRepository.findByMemberId(memberId);
        }

        Long menuId = cartMenuAddRequest.getMenuId();
        //TODO : 장바구니 테스트
        cart.addCartMenu(
                CartMenu.builder()
                        .menuId(menuId)
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

        cartRepository.save(cart);

    }

    public CartSearchResponse searchCartMenuList(Long memberId) {
        Cart cart = cartQueryRepository.findByMemberId(memberId);
        return CartSearchResponse.of(cart);
    }
}
