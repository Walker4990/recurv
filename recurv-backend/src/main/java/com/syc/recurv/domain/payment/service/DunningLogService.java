package com.syc.recurv.domain.payment.service;

import com.syc.recurv.domain.payment.entity.DunningLog;
import com.syc.recurv.domain.payment.repository.DunningLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DunningLogService {
    private final DunningLogRepository dunningLogRepository;

    public DunningLog create(DunningLog log) {
        return dunningLogRepository.save(log);
    }

    public DunningLog get(Long id) {
        return dunningLogRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("독촉 로그 없음"));
    }

    public List<DunningLog> getAll() {
        return dunningLogRepository.findAll();
    }

    public DunningLog update(DunningLog log) {
        return dunningLogRepository.save(log);
    }

    public void delete(Long id) {
        dunningLogRepository.deleteById(id);
    }
}

