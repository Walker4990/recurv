package com.syc.recurv.domain.subscription.service;

import com.syc.recurv.domain.subscription.entity.SubscriptionCoupon;
import com.syc.recurv.domain.subscription.entity.SubscriptionItem;
import com.syc.recurv.domain.subscription.repository.SubscriptionCouponRepository;
import com.syc.recurv.domain.subscription.repository.SubscriptionItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SubscriptionCouponService {
    private final SubscriptionCouponRepository subscriptionCouponRepository;

    //Create
    public SubscriptionCoupon create(SubscriptionCoupon scCoupon){
        return subscriptionCouponRepository.save(scCoupon);
    }
    // Read
    public Optional<SubscriptionCoupon> get(Long id){
        return Optional.ofNullable(subscriptionCouponRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(("구독 없음"))));
    }
    // Update
    public SubscriptionCoupon update(SubscriptionCoupon scCoupon){
        return subscriptionCouponRepository.save(scCoupon);
    }
    // Delete
    public void delete (SubscriptionCoupon scCoupon){
        subscriptionCouponRepository.delete(scCoupon);
    }
    // ReadAll
    public List<SubscriptionCoupon> getAll(){
        return subscriptionCouponRepository.findAll();
    }
}
