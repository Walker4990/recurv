package com.syc.recurv.domain.subscription.repository;

import com.syc.recurv.domain.subscription.entity.Subscription;
import com.syc.recurv.domain.subscription.entity.SubscriptionItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

}
