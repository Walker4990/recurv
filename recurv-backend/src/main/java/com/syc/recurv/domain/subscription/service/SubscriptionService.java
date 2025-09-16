package com.syc.recurv.domain.subscription.service;

import com.syc.recurv.domain.subscription.entity.Subscription;
import com.syc.recurv.domain.subscription.repository.SubscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SubscriptionService {
    private final SubscriptionRepository subscriptionRepository;

    // create
    public Subscription create (Subscription sc){
        return subscriptionRepository.save(sc);
    }

    // Read
    public Subscription get(Long id) {
        return subscriptionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("구독 없음"));
    }
    // Update
    public Subscription update(Subscription sc){
        return subscriptionRepository.save(sc);
    }
    // Delete
    public void delete (Long id){
        subscriptionRepository.deleteById(id);
    }
    // Read All
    public List<Subscription> getAll(){
        return  subscriptionRepository.findAll();
    }
}
