package aba3.lucid.config;

import aba3.lucid.jwt.JwtHandShakeInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@RequiredArgsConstructor
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final JwtHandShakeInterceptor jwtHandShakeInterceptor;

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableStompBrokerRelay("/topic") // STOMP를 통해 RabbitMQ 연동
                .setRelayHost("localhost") //TODO HAProxy파일을 통해 로드밸런싱 진행 해야댐 localhost는 로컬환경에서 사용가능한것
                .setRelayPort(61613)
                .setClientLogin("guest")
                .setClientPasscode("guest");
        config.setApplicationDestinationPrefixes("/chat"); // 메시지 송신 경로 접두사
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws/chat")
                .setAllowedOriginPatterns("*")
                .withSockJS()
                .setInterceptors(jwtHandShakeInterceptor);
    }
}
