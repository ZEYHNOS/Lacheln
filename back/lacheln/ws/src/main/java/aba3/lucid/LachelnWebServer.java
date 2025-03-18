package aba3.lucid;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class LachelnWebServer {
    public static void main(String[] args) {
        SpringApplication.run(LachelnWebServer.class, args);
    }
}
