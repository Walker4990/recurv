package com.syc.recurv.domain.subscription.repository;

import com.syc.recurv.domain.subscription.entity.SubscriptionCoupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscriptionCouponRepository extends JpaRepository<SubscriptionCoupon, Long> {
}
