package aba3.lucid.Calendar.listener;

import aba3.lucid.Calendar.Business.CalendarBusiness;
import aba3.lucid.domain.calendar.dto.CalendarDto;
import aba3.lucid.domain.calendar.dto.CalendarReservation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import com.rabbitmq.client.Channel;

import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
public class CalendarListener {

    private final RabbitTemplate rabbitTemplate;
    private final CalendarBusiness calendarBusiness;

    @RabbitListener(queues = "schedule.request.queue")
    public void calendar(Message message, Channel channel) throws IOException {
        long deliveryTag = message.getMessageProperties().getDeliveryTag();

        try {
            CalendarReservation dto = (CalendarReservation) rabbitTemplate.getMessageConverter().fromMessage(message);
            log.info("Calendar reservation: {}", dto);
            calendarBusiness.userReservation(dto);

            channel.basicAck(deliveryTag, false);
        } catch (Exception e) {
            channel.basicNack(deliveryTag, false, true);
        }
    }

}
