package com.syc.recurv.domain.mail.controller;

import com.syc.recurv.domain.mail.dto.MailDTO;
import com.syc.recurv.domain.mail.service.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/consulting")
public class MailController {
    private final MailService mailService;

    @PostMapping
    public String sendMail(@RequestBody MailDTO mailDTO) {
        mailService.sendConsultingMail(mailDTO.getName(),  mailDTO.getEmail(),
                                    mailDTO.getCompany(), mailDTO.getMessage());
        return "상담 요청 메일 전송 완료!";
    }
}
