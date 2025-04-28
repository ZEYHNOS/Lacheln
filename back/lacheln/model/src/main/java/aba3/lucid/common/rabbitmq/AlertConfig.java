package aba3.lucid.common.rabbitmq;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.DefaultJackson2JavaTypeMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Setter
@Slf4j
@Configuration
@ConfigurationProperties(prefix = "spring.rabbitmq")
public class AlertConfig {

    private String host;

    private String username;

    private String password;

    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory(host);
        connectionFactory.setUsername(username);
        connectionFactory.setPassword(password);
        return connectionFactory;
    }

    @Bean
    public Jackson2JsonMessageConverter jsonMessageConverter() {
        Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter(); // ➤ JSON 메시지 처리용 변환기

        // ➤ 메시지를 어떤 클래스에 매핑할지 명시 (보안 목적)
        DefaultJackson2JavaTypeMapper typeMapper = new DefaultJackson2JavaTypeMapper();

        Map<String, Class<?>> allowedMappings = new HashMap<>();
        allowedMappings.put("aba3.lucid.domain.alert.dto.CompanyAlertDto", aba3.lucid.domain.alert.dto.CompanyAlertDto.class);
        typeMapper.setIdClassMapping(allowedMappings); // ➤ 위에 정의한 클래스만 역직렬화 허용

        converter.setJavaTypeMapper(typeMapper); // ➤ 타입 매핑 정보 적용
        return converter;
    }


    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, Jackson2JsonMessageConverter converter) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(converter);
        rabbitTemplate.setChannelTransacted(true);

        return rabbitTemplate;
    }

    // 자동 ACK 방지: 수동 ACK 모드 설정
    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setAcknowledgeMode(AcknowledgeMode.MANUAL);  // 반드시 수동 ACK으로 변경
        return factory;
    }
}
