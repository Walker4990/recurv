package com.syc.recurv.domain.notification.service;

import lombok.AllArgsConstructor;
import lombok.Data;

// 단순 DTO
@Data
@AllArgsConstructor
public class PartnerContact {
    private String email;
    private String phone;
}
