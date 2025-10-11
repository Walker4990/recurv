package com.syc.recurv.domain.notification.controller;

import com.syc.recurv.domain.notification.dto.NotificationLogResponse;
import com.syc.recurv.domain.notification.service.NotificationAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/notification")
@RequiredArgsConstructor
public class NotificationAdminController {

    private final NotificationAdminService adminService;

    // 전체 로그 조회
    @GetMapping("/log")
    public List<NotificationLogResponse> getAllLogs() {
        return adminService.findAllLogs();
    }
}
