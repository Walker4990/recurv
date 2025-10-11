package com.syc.recurv.domain.users.service;

import com.syc.recurv.domain.invoice.entity.Invoice;
import com.syc.recurv.domain.invoice.repository.InvoiceRepository;
import com.syc.recurv.domain.payment.entity.Payment;
import com.syc.recurv.domain.payment.repository.PaymentRepository;
import com.syc.recurv.domain.users.entity.Users;
import com.syc.recurv.domain.users.repository.UsersRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
@CacheConfig(cacheNames = "partners:all")
@Service
@RequiredArgsConstructor
public class UsersAdminService {
    private final UsersRepository usersRepository;
    private final InvoiceRepository invoiceRepository;
    private final PaymentRepository paymentRepository;

    @PostConstruct
    public void checkProxy() {
        System.out.println("🧩 UsersAdminService proxy class: " + this.getClass());
    }

    // 1. 전체 파트너 조회
    @Cacheable(key = "'allPartners'")
    public List<Users> getAllPartners() {
        System.out.println("💡 [DB Query 실행됨 - 캐시 MISS]");
        System.out.println("🧩 현재 this 클래스 = " + this.getClass());
        return usersRepository.findAll();
    }

    // 2. 단일 파트너 조회
    public Users getPartnerById(Long userId) {
        return usersRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("해당 유저를 찾을 수 없습니다."));
    }

    // 3. 해당 유저의 청구서 목록
    public List<Invoice> getInvoicesByUser(Long userId) {
        return invoiceRepository.findByPartnerNo(userId);
    }

    // 4. 해당 유저의 결제 내역
    public List<Payment> getPaymentsByUser(Long userId) {
        return paymentRepository.findByPartnerNo(userId);
    }
}
