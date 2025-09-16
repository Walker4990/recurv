package com.syc.recurv.domain.billing.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.syc.recurv.domain.billing.entity.BillingPlan;
import com.syc.recurv.domain.billing.repository.BillingPlanRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BillingPlanService {
    private final BillingPlanRepository billingPlanRepository;

    // Create
    public BillingPlan create(BillingPlan billingPlan) {
        return billingPlanRepository.save(billingPlan);
    }

    // Read (단건 조회)
    public BillingPlan get(Long id) {
        return billingPlanRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("요금제 없음"));
    }

    // Read (전체 조회)
    public List<BillingPlan> getAll() {
        return billingPlanRepository.findAll();
    }

    // Update
    public BillingPlan update(BillingPlan billingPlan) {
        return billingPlanRepository.save(billingPlan);
    }

    // Delete
    public void delete(Long id) {
        billingPlanRepository.deleteById(id);
    }
}

