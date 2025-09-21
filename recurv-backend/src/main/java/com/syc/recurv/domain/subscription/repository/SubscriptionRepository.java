package com.syc.recurv.domain.subscription.repository;

import com.syc.recurv.domain.subscription.entity.Subscription;
import com.syc.recurv.domain.subscription.entity.SubscriptionItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

    // 활성화
    @Modifying
    @Query("UPDATE Subscription s SET s.status = 'ACTIVE' WHERE s.partnerNo = :partnerNo")
    void activateByPartnerNo(@Param("partnerNo") Long partnerNo);

    // 비활성화
    @Modifying
    @Query("UPDATE Subscription s SET s.status = 'INACTIVE' WHERE s.partnerNo = :partnerNo")
    void deactivateByPartnerNo(@Param("partnerNo") Long partnerNo);
}

