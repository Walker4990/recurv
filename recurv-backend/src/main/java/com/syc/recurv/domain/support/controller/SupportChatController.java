package com.syc.recurv.domain.support.controller;

import com.syc.recurv.domain.support.entity.SupportMessage;
import com.syc.recurv.domain.support.repository.SupportMessageRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/support")
public class SupportChatController {

    private final SupportMessageRepository repository;
    private final SimpMessagingTemplate smt;

    // WebSocket 메시지 전송
    @MessageMapping("/chat.send")
    public void handleChatMessage(@Payload Map<String, Object> message) {
        Long partnerNo = Long.parseLong(message.get("partnerNo").toString());
        String content = message.get("content").toString();
        String sender = message.get("sender").toString();

        // DB 저장
        SupportMessage msg = repository.save(
                SupportMessage.builder()
                        .partnerNo(partnerNo)
                        .sender(sender)
                        .content(content)
                        .createdAt(LocalDateTime.now())
                        .unread("partner".equals(sender)) // 👈 파트너가 보낸 메시지는 unread=true
                        .build()
        );

        // 1:1 채널로 브로드캐스트
        smt.convertAndSend("/topic/support/" + partnerNo, msg);

        // 신규 문의면 관리자 대시보드 알림용 채널로도 브로드캐스트
        if ("partner".equals(sender)) {
            Map<String, Object> alert = new HashMap<>();
            alert.put("partnerNo", partnerNo);
            alert.put("message", "💬 새로운 문의가 도착했습니다.");
            alert.put("time", LocalDateTime.now().toString());
            smt.convertAndSend("/topic/support/new", alert);
        }
    }

    // 1) 파트너별 가장 최근 메시지 1건씩 반환 (대시보드/목록용)
    @GetMapping("/recent")
    public List<Map<String, Object>> getRecentMessages() {
        // 모든 메시지를 불러온 후, partnerNo 기준으로 그룹화
        List<SupportMessage> allMessages = repository.findAll();

        // partnerNo별로 가장 최근 메시지 1건만 추출
        Map<Long, SupportMessage> latestPerPartner = allMessages.stream()
                .collect(Collectors.toMap(
                        SupportMessage::getPartnerNo,
                        m -> m,
                        (m1, m2) -> m1.getCreatedAt().isAfter(m2.getCreatedAt()) ? m1 : m2
                ));

        // 응답용 데이터 가공
        return latestPerPartner.values().stream()
                .sorted(Comparator.comparing(SupportMessage::getCreatedAt).reversed())
                .map(msg -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("partnerNo", msg.getPartnerNo());
                    map.put("sender", msg.getSender());
                    map.put("content", msg.getContent());
                    map.put("createdAt", msg.getCreatedAt());
                    map.put("unread", msg.isUnread());
                    return map;
                })
                .collect(Collectors.toList());

    }

    // 2) 특정 파트너의 전체 대화 이력 조회
    @Transactional
    @GetMapping("/{partnerNo}")
    public List<SupportMessage> getChatHistory(@PathVariable Long partnerNo) {
        List<SupportMessage> history = repository.findByPartnerNoOrderByCreatedAtAsc(partnerNo);

        // 조회 시 unread 메시지는 읽음 처리
        history.stream()
                .filter(SupportMessage::isUnread)
                .forEach(msg -> msg.setUnread(false));
        repository.saveAll(history);

        return history;
    }
}
