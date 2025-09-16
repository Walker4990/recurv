package com.syc.recurv.domain.coupon.service;

import com.syc.recurv.domain.coupon.entity.Coupon;
import com.syc.recurv.domain.coupon.repository.CouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CouponService {
    private final CouponRepository couponRepository;

    public Coupon create(Coupon coupon) {
        return couponRepository.save(coupon);
    }

    public Coupon get(Long id) {
        return couponRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("쿠폰 없음"));
    }

    public List<Coupon> getAll() {
        return couponRepository.findAll();
    }

    public Coupon update(Coupon coupon) {
        return couponRepository.save(coupon);
    }

    public void delete(Long id) {
        couponRepository.deleteById(id);
    }
}

