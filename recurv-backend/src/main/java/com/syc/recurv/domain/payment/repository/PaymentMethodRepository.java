package com.syc.recurv.domain.payment.repository;

import com.syc.recurv.domain.payment.entity.PaymentMethod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentMethodRepository extends JpaRepository<PaymentMethod,Long> {
}
