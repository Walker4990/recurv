package com.syc.recurv.domain.subscription.service;

import com.syc.recurv.domain.subscription.entity.SubscriptionCouponId;
import com.syc.recurv.domain.subscription.entity.SubscriptionItem;
import com.syc.recurv.domain.subscription.repository.SubscriptionCouponIdRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SubscriptionCouponIdService {

    private final SubscriptionCouponIdRepository subscriptionCouponIdRepository;
    //Create
    public SubscriptionCouponId create(SubscriptionCouponId scItem){
        return subscriptionCouponIdRepository.save(scItem);
    }
    // Read
    public Optional<SubscriptionCouponId> get(Long id){
        return Optional.ofNullable(subscriptionCouponIdRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(("구독 없음"))));
    }
    // Update
    public SubscriptionCouponId update(SubscriptionCouponId couponId){
        return subscriptionCouponIdRepository.save(couponId);
    }
    // Delete
    public void delete (SubscriptionCouponId couponId){
        subscriptionCouponIdRepository.delete(couponId);
    }
    // ReadAll
    public List<SubscriptionCouponId> getAll(){
        return subscriptionCouponIdRepository.findAll();
    }


}
