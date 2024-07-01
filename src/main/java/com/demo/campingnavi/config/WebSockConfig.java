package com.demo.campingnavi.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.*;

@Slf4j
@RequiredArgsConstructor
@Configuration
@EnableWebSocketMessageBroker // stomp 사용을위해 선언
public class WebSockConfig implements WebSocketMessageBrokerConfigurer {

    // WebSocketMessageBrokerConfigurer를 상송받아 메세지브로커를 구현
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/sub");
        config.setApplicationDestinationPrefixes("/pub");
    } // pub/sub 메시징을 구현하기 위해 메세지발행요청의 prefix를 /pub로 시작
      // 구독요청의 prefix는 sub로 시작

    // stomp웹소켓 연결의 엔드포인트는 ws-stomp로 설정(개발서버의 접속주소는 ws://localhost:8080/ws-stomp)
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws-stomp").setAllowedOriginPatterns("*").withSockJS();
        // setAllowedOrigins("*") - 다른서버에서도 접속 가능하도록 CORS
    }
}
