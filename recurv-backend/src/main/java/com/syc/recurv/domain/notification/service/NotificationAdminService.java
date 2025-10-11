package com.syc.recurv.domain.notification.service;

import com.syc.recurv.domain.notification.dto.NotificationLogResponse;
import com.syc.recurv.domain.notification.repository.NotificationLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor

public class NotificationAdminService {

    private final NotificationLogRepository logRepository;

    public List<NotificationLogResponse> findAllLogs() {
        return logRepository.findAll()
                .stream()
                .map(NotificationLogResponse::from)
                .collect(Collectors.toList());
    }
}
