package com.syc.recurv.config;

import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.security.config.annotation.web.messaging.MessageSecurityMetadataSourceRegistry;
import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer;

public class WebSocketSecurityConfig extends AbstractSecurityWebSocketMessageBrokerConfigurer {

    @Override
    protected void configureInbound(MessageSecurityMetadataSourceRegistry msg) {
        msg
                .simpDestMatchers("/app/**").permitAll()  // ✅ 클라이언트에서 보낸 STOMP 메시지 허용
                .simpSubscribeDestMatchers("/topic/**").permitAll()  // ✅ 구독 허용
                .anyMessage().permitAll(); // 나머지도 전부 허용
    }
    @Override
    protected boolean sameOriginDisabled() {
        return true; // ✅ CORS 비활성화
    }

    @Override
    public void customizeClientInboundChannel(ChannelRegistration registration) {
        // 특별한 인증 처리 안 함
    }
}
