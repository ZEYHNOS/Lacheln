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

    // 메시지 브로커에 대한 세팅
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.setApplicationDestinationPrefixes("/chat"); // 메시지 송신 경로 접두사(메시지 브로커에 전달하기 위해선 요청의 /chat으로 시작해야함)
        config.enableStompBrokerRelay("/topic") // STOMP를 통해 RabbitMQ 연동
                .setRelayHost("lacheln.p-e.kr")      //TODO HAProxy파일을 통해 로드밸런싱 진행 해야댐 localhost는 로컬환경에서 사용가능한것
                .setRelayPort(61613)            // RabbitMQ 포트번호
                .setClientLogin("guest")        // RabbitMQ ID
                .setClientPasscode("guest");    // RabbitMQ PW
    }

    // Stomp 프로토콜에 최초로 구독할 수 있는 엔드포인트 등록
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws/chat")
                .setAllowedOriginPatterns("*")
                .withSockJS()
                .setInterceptors(jwtHandShakeInterceptor);
    }
}
