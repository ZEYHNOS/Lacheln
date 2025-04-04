package aba3.lucid.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

    // RedisTemplate Bean으로 주입
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
