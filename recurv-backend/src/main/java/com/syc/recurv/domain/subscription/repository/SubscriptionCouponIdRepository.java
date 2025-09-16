package com.syc.recurv.domain.subscription.repository;

import com.syc.recurv.domain.subscription.entity.SubscriptionCouponId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscriptionCouponIdRepository extends JpaRepository<SubscriptionCouponId,Long> {
}
