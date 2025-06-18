package aba3.lucid;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@EnableAsync
@EnableRabbit
@EnableWebMvc
@EnableScheduling
@SpringBootApplication
public class LachelnWebServer {
    public static void main(String[] args) {
        SpringApplication.run(LachelnWebServer.class, args);
    }
}
