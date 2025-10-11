package com.syc.recurv.domain.notification.service;

import org.springframework.stereotype.Service;

@Service
public class PartnerInfoService {

    // TODO: ERP DB 연동 or API 조회
    public PartnerContact findContactByPartnerNo(Long partnerNo) {
        // 예시: 임시 데이터
        if (partnerNo == 1L) {
            return new PartnerContact("partner1@example.com", "01012345678");
        }
        return null;
    }
}
