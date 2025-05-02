package aba3.lucid.payment.service;

import aba3.lucid.cart.service.CartService;
import aba3.lucid.common.exception.ApiException;
import aba3.lucid.common.status_code.ErrorCode;
import aba3.lucid.domain.cart.entity.CartEntity;
import aba3.lucid.domain.coupon.dto.CouponVerifyRequest;
import aba3.lucid.domain.payment.dto.PaymentVerifyRequest;
import aba3.lucid.domain.payment.entity.PayManagementEntity;
import aba3.lucid.domain.payment.repository.PayManagementRepository;
import aba3.lucid.domain.product.dto.ProductSnapshot;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentService {

    private final RabbitTemplate rabbitTemplate;

    private final CartService cartService;

    private final PayManagementRepository payManagementRepository;

    @Transactional
    public PayManagementEntity save(PayManagementEntity entity) {
        // 가격 확인하기

        // 쿠폰 정합성 검사하기

        // 일정이 요청 사이에 예약 되었는지 확인하기
        // 만약 그 사이에 일정이 등록되었다면 환불 로직을 실행하기

        // 전부 확인이 되었다면 저장하고 쿠폰 사용으로 처리하고 일정에 등록하기

        return payManagementRepository.save(entity);
    }

    public PayManagementEntity refund(String id) {
        PayManagementEntity entity = findByIdWithThrow(id);

        entity.refund();
        // TODO 환불 로직
        /**
         * 1. 결제 취소(환불) 요청 PG사로 보내기
         * 2. 주문 상태를 실패(FAIL)로 변경하기
         * 3. 사용자에게 실패 알림 보내기
         * 4. 쿠폰 복구, 재고 복구 등 후처리하기
         */

        return payManagementRepository.save(entity);
    }

    public String generateMerchantUid() {
        return "order-" + LocalDate.now() + "-" + UUID.randomUUID();
    }


    public PayManagementEntity findByIdWithThrow(String id) {
        return payManagementRepository.findById(id)
                .orElseThrow(() -> new ApiException(ErrorCode.BAD_REQUEST));
    }

    public Object verification(PaymentVerifyRequest request, String userId) {
        // TODO 일정 확인하기
        List<CartEntity> cartEntityList = cartService.findAllById(request.getCardIdList());


        // 상품 스냅샷 검사하기
        List<ProductSnapshot> productSnapshotList = cartEntityList.stream()
                .map(ProductSnapshot::new)
                .toList()
                ;

        // TODO RPC 패턴 사용하기

        // 쿠폰 유효성 검사하기
        // userId, couponBoxIdList, productId, totalAmount
        // 상품 아이디 리스트
        List<Long> productIdList = cartEntityList.stream()
                .map(CartEntity::getProductId)
                .toList()
                ;

        BigInteger total = BigInteger.ZERO;
        productSnapshotList.forEach(it -> total.add(it.getPrice()));


        // 쿠폰 유효성 보내기
        String correlationId = UUID.randomUUID().toString();
        MessageProperties props = new MessageProperties();
        props.setReplyTo("to.user");
        props.setCorrelationId(correlationId);

        CouponVerifyRequest requestMessage = CouponVerifyRequest.builder()
                .userId(userId)
                .couponBoxIdList(request.getCouponBoxIdList())
                .productIdList(productIdList)
                .amount(total)
                .build()
                ;
        Message message = rabbitTemplate.getMessageConverter().toMessage(requestMessage, props);
        log.info("Message Send : {}", message);
        rabbitTemplate.send("company.exchange", "to.company", message);

        Message reply = rabbitTemplate.receive("to.user", 5000);
        log.info("Message reply : {}", reply);

        if (reply != null && correlationId.equals(reply.getMessageProperties().getCorrelationId())) {
            // 응답 메시지를 객체로 변환
            CouponVerifyRequest result = (CouponVerifyRequest) rabbitTemplate.getMessageConverter().fromMessage(reply);
            log.info("RPC result: {}", result);
        } else {
            // 타임아웃 처리
            log.error("Timeout: No response received within 5 seconds");
            throw new RuntimeException("No response received within timeout period.");
        }

        return null;
    }
}
