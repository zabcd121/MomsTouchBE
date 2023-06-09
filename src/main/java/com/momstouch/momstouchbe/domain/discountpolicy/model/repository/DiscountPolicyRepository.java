package com.momstouch.momstouchbe.domain.discountpolicy.model.repository;

import com.momstouch.momstouchbe.domain.discountpolicy.model.DiscountPolicy;
import com.momstouch.momstouchbe.domain.discountpolicy.model.repository.command.DiscountPolicyCommandRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiscountPolicyRepository extends JpaRepository<DiscountPolicy,Long>, DiscountPolicyCommandRepository {
}
