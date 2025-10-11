package com.syc.recurv.domain.users.controller;

import com.syc.recurv.domain.invoice.entity.Invoice;
import com.syc.recurv.domain.payment.entity.Payment;
import com.syc.recurv.domain.users.entity.Users;
import com.syc.recurv.domain.users.repository.UsersRepository;
import com.syc.recurv.domain.users.service.UsersAdminService;
import com.syc.recurv.domain.users.service.UsersService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
@Slf4j
@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/admin/partner")
public class AdminUserController {
    private final UsersRepository usersRepository;
    private final UsersAdminService usersAdminService;
    @GetMapping
    public ResponseEntity<List<Users>> getAllPartners(){
        return ResponseEntity.ok(usersRepository.findAll());
    }
    @GetMapping("/cache")
    public ResponseEntity<List<Users>> getAllUsers(){
        log.info("▶ /api/admin/partner/cache 진입 성공");
        return ResponseEntity.ok(usersAdminService.getAllPartners());
    }
    // 고객사 상세 조회
    @GetMapping("/{userId}")
    public ResponseEntity<Users> getPartner(@PathVariable Long userId){
        log.info("▶ AdminPartnerController.getPartner() 호출됨 userId={}", userId);
        return ResponseEntity.ok(usersAdminService.getPartnerById(userId));
    }
    @GetMapping("/{userId}/invoices")
    public ResponseEntity<List<Invoice>> getPartnerInvoices(@PathVariable Long userId){
        return ResponseEntity.ok(usersAdminService.getInvoicesByUser(userId));
    }
    @GetMapping("/{userId}/payments")
    public ResponseEntity<List<Payment>> getPartnerPayments(@PathVariable Long userId){
        return ResponseEntity.ok(usersAdminService.getPaymentsByUser(userId));
    }
    @PutMapping("/{userId}")
    public ResponseEntity<Users> updatePartner(@PathVariable Long userId, @RequestBody Map<String, String> body){
        String status = body.get("status");
        Users user = usersRepository.findById(userId)
                .orElseThrow(()-> new RuntimeException("해당 고객님을 찾을 수 없습니다."));
        user.setStatus(status);
        usersRepository.save(user);
        return ResponseEntity.ok(user);
    }
}
