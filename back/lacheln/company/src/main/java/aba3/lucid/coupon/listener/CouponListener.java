package aba3.lucid.coupon.listener;

import aba3.lucid.common.validate.Validator;
import aba3.lucid.coupon.service.CouponBoxService;
import aba3.lucid.coupon.service.CouponService;
import aba3.lucid.domain.coupon.dto.CouponVerifyRequest;
import aba3.lucid.domain.coupon.entity.CouponBoxEntity;
import aba3.lucid.domain.product.entity.ProductEntity;
import aba3.lucid.product.service.ProductService;
import com.rabbitmq.client.Channel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CouponListener {

    private final RabbitTemplate rabbitTemplate;
    private final CouponService couponService;
    private final CouponBoxService couponBoxService;
    private final ProductService productService;

    // 사용자가 결제 전 쿠폰 유효성 확인하기
    // RabbitMQ 연결하기 RPC 스타일로
    @RabbitListener(queues = "verify.coupon")
    public void couponVerify(Message message, Channel channel) throws IOException {
        Validator.throwIfNull(message);

        String replyTo = message.getMessageProperties().getReplyTo();
        String correlationId = message.getMessageProperties().getCorrelationId();


        CouponVerifyRequest request = (CouponVerifyRequest) rabbitTemplate.getMessageConverter().fromMessage(message);


        if (request.getCouponBoxIdList().isEmpty()) {
            // TODO 응답값 넣기
            return;
        }

        String userId = request.getUserId();
        List<CouponBoxEntity> couponBoxEntityList = couponBoxService.findAllById(request.getCouponBoxIdList());
        List<ProductEntity> productEntityList = productService.findAllById(request.getProductIdList());

        couponService.verificationBeforePayment(userId, couponBoxEntityList, productEntityList);

        long deliveryTag = message.getMessageProperties().getDeliveryTag();

        // 응답 메시지 속성 설정
        MessageProperties replyProps = new MessageProperties();
        replyProps.setCorrelationId(correlationId);

        // 응답 메시지 생성
        Message replyMessage = rabbitTemplate.getMessageConverter().toMessage(request, replyProps);

        // 응답 메시지 전송
        rabbitTemplate.send(replyTo, replyMessage);
        channel.basicAck(deliveryTag, false);
        log.info("rpc rabbitmq end");
    }

}
