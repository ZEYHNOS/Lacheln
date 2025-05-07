package aba3.lucid.payment.service;

import aba3.lucid.cart.service.CartService;
import aba3.lucid.common.exception.ApiException;
import aba3.lucid.common.status_code.ErrorCode;
import aba3.lucid.domain.cart.entity.CartEntity;
import aba3.lucid.domain.coupon.dto.CouponVerifyRequest;
import aba3.lucid.domain.coupon.dto.CouponVerifyResponse;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    // 결제 전 검증 및 총 금액 반환
    public BigInteger verificationAndGetTotalAmount(PaymentVerifyRequest request, String userId) {
        // TODO 일정 확인하기

        // 카트 Entity List
        List<CartEntity> cartEntityList = cartService.findAllById(request.getCardIdList());

        // 쿠폰 확인을 위해 메세지 보내기
        CouponVerifyResponse response = couponVerificationSendMessage(userId, request, cartEntityList);



        // TODO RPC 패턴 사용하기


//        return getTotalAmount(cartEntityList, response);
        return null;
    }

    // 쿠폰 유효성 검사하기
    // userId, couponBoxIdList, productId, totalAmount
    private CouponVerifyResponse couponVerificationSendMessage(String userId, PaymentVerifyRequest request, List<CartEntity> cartEntityList) {
        // 상품 스냅샷 리스트
        List<ProductSnapshot> productSnapshotList = cartEntityList.stream()
                .map(ProductSnapshot::new)
                .toList()
                ;

        // 상품 아이디 리스트
        List<Long> productIdList = cartEntityList.stream()
                .map(CartEntity::getProductId)
                .toList()
                ;


        // 메세지 생성 전 설정하기
        String correlationId = UUID.randomUUID().toString();
        MessageProperties props = new MessageProperties();
        props.setReplyTo("to.user"); // 응답 결과 받는 큐 TODO 다른 큐로 설정하기
        props.setCorrelationId(correlationId);

        // 메세지 보낼 DTO
        CouponVerifyRequest requestMessage = CouponVerifyRequest.builder()
                .userId(userId)
                .couponBoxIdList(request.getCouponBoxIdList())
                .productIdList(productIdList)
                .amount(null)
                .build()
                ;

        // 메세지 생성 및 보내기
        Message message = rabbitTemplate.getMessageConverter().toMessage(requestMessage, props);
        log.info("Message Send : {}", message);
        rabbitTemplate.send("company.exchange", "to.company", message);

        // 메세지 받기(Timeout : 5초)
        Message reply = rabbitTemplate.receive("to.user", 5000);
        log.info("Message reply : {}", reply);

        // TODO 쿠폰 Response로 받아서 결제해야 하는 금액 계산하기
        // 필요한 필드 : 쿠폰 할인율, companyID

        // 성공적으로 통신 했을 때(쿠폰 유효성 통과)
        if (reply != null && correlationId.equals(reply.getMessageProperties().getCorrelationId())) {
            // 응답 메시지를 객체로 변환
            CouponVerifyResponse result = (CouponVerifyResponse) rabbitTemplate.getMessageConverter().fromMessage(reply);
            log.info("RPC result: {}", result);
            return result;
        }
        // TODO 통신은 성공했지만 쿠폰 유효성 검사 실패했을 때

        // 타임아웃일 때
        else {
            // 타임아웃 처리
            log.error("Timeout: No response received within 5 seconds");
            throw new RuntimeException("No response received within timeout period.");
        }
    }

    public List<PayManagementEntity> getPaymentList(String userId) {
        return payManagementRepository.findAllByUser_userId(userId);
    }

//    // 결제해야 하는 총 금액
//    protected BigInteger getTotalAmount(List<CartEntity> cartEntityList, CouponVerifyResponse response) {
//        // 업체 별 사용되는 돈
//        Map<Long, BigInteger> groupByCompanyAmountMap = new HashMap<>();
//
//        for (CartEntity cart : cartEntityList) {
//            groupByCompanyAmountMap.put(
//                    cart.getCpId(),
//                    groupByCompanyAmountMap.getOrDefault(cart.getCpId(), BigInteger.ZERO).add(cart.getPrice())
//            );
//        }
//
//        // 할인 된 금액 총합
//        BigInteger totalAmount = BigInteger.ZERO;
//        for (Long companyId : groupByCompanyAmountMap.keySet()) {
//            int discountRate = response.getKeyCompanyIdValueDiscountRate().getOrDefault(companyId, 0);
//
//            BigInteger companyAmount = groupByCompanyAmountMap.get(companyId);
//            BigInteger discountedAmount;
//            if (discountRate > 0) {
//                discountedAmount = companyAmount.multiply(BigInteger.valueOf(100))
//                        .divide(BigInteger.valueOf(discountRate));
//            } else {
//                // 할인 없음 -> 원금 그대로
//                discountedAmount = companyAmount;
//            }
//
//            totalAmount = totalAmount.add(discountedAmount);
//
//        }
//
//        return totalAmount;
//    }
}
