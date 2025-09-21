package com.syc.recurv.domain.payment.util;

import org.springframework.beans.factory.annotation.Value;

import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Component
public class SignatureValidator {
    private final String secretKey;
    public SignatureValidator(@Value("${toss.webhook.secret}") String secretKey) {
        this.secretKey = secretKey;
    }

    public boolean verify(String payload, String signature) {
        try {
            Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(), "HmacSHA256");
            sha256_HMAC.init(secretKeySpec);

            String expected = Base64.getEncoder().encodeToString(
                    sha256_HMAC.doFinal(payload.getBytes(StandardCharsets.UTF_8))
            );
            return expected.equals(signature);
        } catch (Exception e) {
            return false;
        }
    }
}
