package com.syc.recurv.domain.billing.repository;


import com.syc.recurv.domain.billing.entity.BillingPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BillingPlanRepository extends JpaRepository<BillingPlan, Long> {
    // 추가 메서드 예시: findByName
    BillingPlan findByName(String name);
}

