package com.syc.recurv.domain.subscription.service;

import com.syc.recurv.domain.subscription.entity.SubscriptionItem;
import com.syc.recurv.domain.subscription.repository.SubscriptionItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SubscriptionItemService {
    private final SubscriptionItemRepository subscriptionItemRepository;

    //Create
    public SubscriptionItem create(SubscriptionItem scItem){
        return subscriptionItemRepository.save(scItem);
    }
    // Read
    public Optional<SubscriptionItem> get(Long id){
        return Optional.ofNullable(subscriptionItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(("구독 없음"))));
    }
    // Update
    public SubscriptionItem update(SubscriptionItem scItem){
        return subscriptionItemRepository.save(scItem);
    }
    // Delete
    public void delete (SubscriptionItem scItem){
        subscriptionItemRepository.delete(scItem);
    }
    // ReadAll
    public List<SubscriptionItem> getAll(){
        return subscriptionItemRepository.findAll();
    }
}
