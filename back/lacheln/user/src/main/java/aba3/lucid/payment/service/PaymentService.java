package aba3.lucid.payment.service;

import aba3.lucid.cart.service.CartService;
import aba3.lucid.common.exception.ApiException;
import aba3.lucid.common.status_code.ErrorCode;
import aba3.lucid.common.status_code.PaymentErrorCode;
import aba3.lucid.domain.cart.dto.CartPaymentRequest;
import aba3.lucid.domain.cart.entity.CartEntity;
import aba3.lucid.domain.coupon.dto.CouponVerifyRequest;
import aba3.lucid.domain.coupon.dto.CouponVerifyResponse;
import aba3.lucid.domain.payment.dto.PaymentVerifyRequest;
import aba3.lucid.domain.payment.entity.PayManagementEntity;
import aba3.lucid.domain.payment.enums.PaymentStatus;
import aba3.lucid.domain.payment.repository.PayManagementRepository;
import aba3.lucid.domain.product.dto.ProductSnapshot;
import aba3.lucid.domain.user.entity.UsersEntity;
import aba3.lucid.user.service.UserService;
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
    private final UserService userService;
    private final PayDetailService payDetailService;

    private final PayManagementRepository payManagementRepository;

    // PaymentErrorCode.BAD_REQUEST => ExceptionHandler 에서 환불 로직 실행
    @Transactional
    public PayManagementEntity save(PayManagementEntity entity, List<Long> cartIdList) {
        // 만약 취소 되었다면 취소 메세지 보내기
        if (entity.getPayStatus().equals(PaymentStatus.Cancel)) {
            throw new ApiException(PaymentErrorCode.CANCEL);
        }

        // 마일리지 차감하기
        userService.deductMileage(entity.getUser(), entity.getPayMileage());

        // 장바구니에서 삭제하기
        cartService.removeCart(cartIdList);

        // TODO RABBITMQ SETTING

        // 업체에 알림 보내기
        rabbitTemplate.convertAndSend("메세지 객체 생성한 후 보내기");

        // 업체 캘린더에 데이터 넣기
        rabbitTemplate.convertAndSend("캘린더 용 데이터 만들어서 보내기");

        // 마일리지 추가하기
        UsersEntity user = entity.getUser();
        user.addMileage(entity.getPayTotalPrice());

        return payManagementRepository.save(entity);
    }

    @Transactional
    public PayManagementEntity refund(PayManagementEntity entity) {

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

    // MerchantID 발급
    public String generateMerchantUid() {
        return "order-" + LocalDate.now() + "-" + UUID.randomUUID();
    }


    public PayManagementEntity findByIdWithThrow(String id) {
        return payManagementRepository.findById(id)
                .orElseThrow(() -> new ApiException(ErrorCode.BAD_REQUEST));
    }

    // 결제 전 검증 및 총 금액 반환
    public BigInteger verificationAndGetTotalAmount(PaymentVerifyRequest request, String userId) {
        // 예약이 가능한지
//        payDetailService.checkReservation(request.getCardRequestList());

        // 카트 Entity List
        List<Long> cartIdList = request.getCardRequestList().stream()
                .map(CartPaymentRequest::getCartId)
                .toList();
        List<CartEntity> cartEntityList = cartService.findAllById(cartIdList);

        // 업체마다 그룹을 만들고 업체 별 결제해야 하는 금액 더하기
        Map<Long, BigInteger> groupByCompanyAmountMap = initGroupByCompanyAmount(cartEntityList);

        // 쿠폰 확인을 위해 메세지 보내기
        CouponVerifyResponse response = couponVerificationSendMessage(userId, request, cartEntityList, groupByCompanyAmountMap);

        // 마일리지 확인하기
        verifyMileage(userId, request.getMileage());

        // Redis를 사용하여 일정 블락 하기

        // 총액에서 쿠폰으로 할인 금액 빼고 마일리지 뺀 금액
        return getTotalAmount(response, request.getMileage(), groupByCompanyAmountMap);
    }

    // 쿠폰 유효성 검사하기
    // userId, couponBoxIdList, productId, totalAmount
    private CouponVerifyResponse couponVerificationSendMessage(String userId, PaymentVerifyRequest request, List<CartEntity> cartEntityList, Map<Long, BigInteger> groupByCompanyAmount) {
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
                .amount(groupByCompanyAmount)
                .build()
                ;

        // 메세지 생성 및 보내기
        Message message = rabbitTemplate.getMessageConverter().toMessage(requestMessage, props);
        rabbitTemplate.send("company.exchange", "to.company", message);

        // 메세지 받기(Timeout : 5초)
        Message reply = rabbitTemplate.receive("to.user", 5000);


        // 성공적으로 통신 했을 때(쿠폰 유효성 통과)
        if (reply != null && correlationId.equals(reply.getMessageProperties().getCorrelationId())) {
            // 응답 메시지를 객체로 변환
            CouponVerifyResponse result = (CouponVerifyResponse) rabbitTemplate.getMessageConverter().fromMessage(reply);
            return result;
        }
        // 타임아웃일 때
        else {
            // 타임아웃 처리
            log.error("Timeout: No response received within 5 seconds");
            throw new ApiException(ErrorCode.SERVER_ERROR);
        }
    }

    public List<PayManagementEntity> getUserPaymentList(String userId) {
        return payManagementRepository.findAllByUser_userId(userId);
    }

    // 마일리지 검증하기
    private void verifyMileage(String userId, BigInteger mileage) {
        UsersEntity user = userService.findByIdWithThrow(userId);

        if (user.getUserMileage().compareTo(mileage) < 0) {
            throw new ApiException(ErrorCode.BAD_REQUEST, "마일리지가 부족합니다.");
        }
    }

    // 결제해야 하는 총 금액
    private BigInteger getTotalAmount(CouponVerifyResponse response, BigInteger mileage, Map<Long, BigInteger> groupByCompanyAmountMap) {

        // 할인 된 금액 총합
        BigInteger totalAmount = BigInteger.ZERO;
        for (Long companyId : groupByCompanyAmountMap.keySet()) {
            // 할인율
            int discountRate = response.getKeyCompanyIdValueDiscountRate().getOrDefault(companyId, 0);

            BigInteger companyAmount = groupByCompanyAmountMap.get(companyId);
            BigInteger discountedAmount;
            if (discountRate > 0) {
                discountedAmount = companyAmount.multiply(BigInteger.valueOf(100 - discountRate))
                        .divide(BigInteger.valueOf(100));
            } else {
                // 할인 없음 -> 원금 그대로
                discountedAmount = companyAmount;
            }

            totalAmount = totalAmount.add(discountedAmount);

        }

        // 마일리지 사용 시 음수 값이 나올 때
        if (totalAmount.compareTo(mileage) < 0) {
            return BigInteger.ZERO;
        }

        return totalAmount;
    }

    // 업체를 그룹으로 생성하고 해당 업체 별 결제해야하는 금액을 계산
    private Map<Long, BigInteger> initGroupByCompanyAmount(List<CartEntity> cartEntityList) {
        Map<Long, BigInteger> groupByCompanyAmountMap = new HashMap<>();

        for (CartEntity cart : cartEntityList) {
            groupByCompanyAmountMap.put(
                    cart.getCpId(),
                    groupByCompanyAmountMap.getOrDefault(cart.getCpId(), BigInteger.ZERO).add(cart.getPrice())
            );
        }

        return groupByCompanyAmountMap;
    }
}
