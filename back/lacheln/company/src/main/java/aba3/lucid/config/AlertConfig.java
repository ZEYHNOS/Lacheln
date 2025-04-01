package aba3.lucid.config;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AlertConfig {

    @Bean
    public RabbitTemplate rabbitTemplate() {
        return new RabbitTemplate();
    }
}
