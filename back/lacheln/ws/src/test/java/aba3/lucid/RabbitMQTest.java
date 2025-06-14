package aba3.lucid;

import aba3.lucid.domain.payment.dto.PopularDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest(classes = LachelnWebServer.class)
public class RabbitMQTest {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Test
    public void sendMessage() {
        for (int i = 1; i <= 5; i++) {
            String message = "Test Message " + i;
            System.out.println("▶ Sending: " + message);
            rabbitTemplate.convertAndSend("company.exchange", "company", message);
        }
    }

    @Test
    public void popularTestMessage() throws JsonProcessingException {
        List<PopularDto> dtoList = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            dtoList.add(PopularDto.builder()
                            .companyId(1L)
                            .productId(1L)
                            .rank(i)
                    .build());
        }

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(dtoList);
        rabbitTemplate.convertAndSend("product.exchange", "popular", json);
    }

}
