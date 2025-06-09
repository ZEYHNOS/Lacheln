package aba3.lucid.payment.service;

import aba3.lucid.cart.service.CartService;
import aba3.lucid.common.exception.ApiException;
import aba3.lucid.common.status_code.ErrorCode;
import aba3.lucid.domain.calendar.dto.CalendarReservation;
import aba3.lucid.domain.cart.entity.CartDetailEntity;
import aba3.lucid.domain.cart.entity.CartEntity;
import aba3.lucid.domain.coupon.dto.CouponVerifyRequest;
import aba3.lucid.domain.coupon.dto.CouponVerifyResponse;
import aba3.lucid.domain.payment.converter.PayDetailConverter;
import aba3.lucid.domain.payment.dto.PaymentVerifyRequest;
import aba3.lucid.domain.payment.entity.PayDetailEntity;
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
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentService {

    private final RabbitTemplate rabbitTemplate;

    private final CartService cartService;
    private final UserService userService;
    private final PayDetailService payDetailService;

    private final PayManagementRepository payManagementRepository;

    private final PayDetailConverter payDetailConverter;

    // PaymentErrorCode.BAD_REQUEST => ExceptionHandler 에서 환불 로직 실행
    @Transactional
    public PayManagementEntity save(PayManagementEntity entity, List<Long> cartIdList) {
        // 만약 취소 되었다면 취소 메세지 보내기
        if (entity.getPayStatus().equals(PaymentStatus.CANCEL)) {
            return payManagementRepository.save(entity);
        }

        // 마일리지 차감하기
        userService.deductMileage(entity.getUser(), entity.getPayMileage());

        // 장바구니에서 삭제하기
        cartService.removeCart(cartIdList);


        // 업체 캘린더에 데이터 넣기
        for (PayDetailEntity payDetail : entity.getPayDetailEntityList()) {
            CalendarReservation dto = CalendarReservation.builder()
                    .title("상품 예약")
                    .content("내용????")
                    .start(payDetail.getStartDatetime())
                    .end(payDetail.getEndDatetime())
                    .memo("토트넘 우승 축하해~")
                    .optionDtoList(payDetailConverter.toDtoList(payDetail.getPayDetailOptionEntityList()))
                    .managerName(payDetail.getManager())
                    .phoneNum(entity.getUser().getUserPhone())
                    .companyId(payDetail.getCpId()  )
                    .userName(payDetail.getPayManagement().getUser().getUserName())
                    .payDtId(payDetail.getPayDetailId())
                    .productName(payDetail.getProductName())
                    .build()
                    ;


            rabbitTemplate.convertAndSend("company.exchange", "company.schedule", dto);
        }

        // 마일리지 추가하기
        UsersEntity user = entity.getUser();
        user.addMileage(entity.getPayTotalPrice());

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
        List<Long> cartIdList = request.getCartIdList();

        List<CartEntity> cartEntityList = cartService.findAllById(cartIdList);

        // RabbitMQ 스냅샷 검증

        // 업체마다 그룹을 만들고 업체 별 결제해야 하는 금액 더하기
        Map<Long, BigInteger> groupByCompanyAmountMap = initGroupByCompanyAmount(cartEntityList);

        // 쿠폰 확인을 위해 메세지 보내기
//        CouponVerifyResponse response = couponVerificationSendMessage(userId, request, cartEntityList, groupByCompanyAmountMap);

        // 마일리지 확인하기
        verifyMileage(userId, request.getMileage());

        // Redis를 사용하여 일정 블락 하기

        // 총액에서 쿠폰으로 할인 금액 빼고 마일리지 뺀 금액
        return getTotalAmount(null, request.getMileage(), groupByCompanyAmountMap);
    }

    // 쿠폰 유효성 검사하기
    // userId, couponBoxIdList, productId, totalAmount
    private CouponVerifyResponse couponVerificationSendMessage(String userId, PaymentVerifyRequest request, List<CartEntity> cartEntityList, Map<Long, BigInteger> groupByCompanyAmount) {
        // 상품 스냅샷 리스트
        List<ProductSnapshot> productSnapshotList = cartEntityList.stream()
                .map(ProductSnapshot::new)
                .toList()
                ;

        // 상품 ID 리스트
        List<Long> productIdList = cartEntityList.stream()
                .map(CartEntity::getPdId)
                .toList()
                ;
        // 메세지 보낼 DTO
        CouponVerifyRequest requestMessage = CouponVerifyRequest.builder()
                .userId(userId)
                .couponBoxIdList(request.getCouponBoxIdList())
                .productIdList(productIdList)
                .amount(groupByCompanyAmount)
                .build()
                ;

        // 스냅샷 검증
        if (!verifySnapshot(productSnapshotList)) {
            throw new ApiException(ErrorCode.BAD_REQUEST, "스냅샷 정보가 다름");
        }

        // 쿠폰 검증
        return verifyCoupon(requestMessage);
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

    // 스냅샷 검증
    private boolean verifySnapshot(List<ProductSnapshot> snapshotList) {
        // 메세지 생성 전 설정하기
        String correlationId = UUID.randomUUID().toString();
        MessageProperties props = new MessageProperties();
        props.setReplyTo("snapshot.queue");
        props.setCorrelationId(correlationId);

        // 메세지 생성 및 보내기
        Message message = rabbitTemplate.getMessageConverter().toMessage(snapshotList, props);
        rabbitTemplate.send("company.exchange", "product.verify", message);

        // 메세지 받기(Timeout : 5초)
        Message reply = rabbitTemplate.receive("snapshot.queue.response", 5000);


        // 성공적으로 통신 했을 때
        if (reply != null && correlationId.equals(reply.getMessageProperties().getCorrelationId())) {
            return (boolean) rabbitTemplate.getMessageConverter().fromMessage(reply);
        }

        return false;
    }

    // 쿠폰 검증
    private CouponVerifyResponse verifyCoupon(CouponVerifyRequest requestMessage) {
        // 메세지 생성 전 설정하기
        String correlationId = UUID.randomUUID().toString();
        MessageProperties props = new MessageProperties();
        props.setReplyTo("from.coupon");
        props.setCorrelationId(correlationId);

        // 메세지 생성 및 보내기
        Message message = rabbitTemplate.getMessageConverter().toMessage(requestMessage, props);
        rabbitTemplate.send("company.exchange", "coupon.verify", message);

        // 메세지 받기(Timeout : 5초)
        Message reply = rabbitTemplate.receive("from.coupon", 5000);


        // 성공적으로 통신 했을 때(쿠폰 유효성 통과)
        if (reply != null && correlationId.equals(reply.getMessageProperties().getCorrelationId())) {
            // 응답 메시지를 객체로 변환
            return (CouponVerifyResponse) rabbitTemplate.getMessageConverter().fromMessage(reply);
        }

        return null;
    }

    // 결제해야 하는 총 금액
    private BigInteger getTotalAmount(CouponVerifyResponse response, BigInteger mileage, Map<Long, BigInteger> groupByCompanyAmountMap) {

        // 할인 된 금액 총합
        BigInteger totalAmount = BigInteger.ZERO;
        for (Long companyId : groupByCompanyAmountMap.keySet()) {
            // 할인율
            int discountRate = 0;
            if (response != null ) {
                 discountRate = response.getKeyCompanyIdValueDiscountRate().getOrDefault(companyId, 0);
            }

            BigInteger companyAmount = groupByCompanyAmountMap.get(companyId);
            BigInteger discountedAmount;
            if (response != null && discountRate > 0) {
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

        return totalAmount.subtract(mileage);
    }

    // 업체를 그룹으로 생성하고 해당 업체 별 결제해야하는 금액을 계산
    private Map<Long, BigInteger> initGroupByCompanyAmount(List<CartEntity> cartEntityList) {
        Map<Long, BigInteger> groupByCompanyAmountMap = new HashMap<>();
        Set<Long> packIdSet = new HashSet<>();

        for (CartEntity cart : cartEntityList) {
            BigInteger price = cart.getPrice();
            if (cart.getPackId() != null) {

                if (!packIdSet.add(cart.getPackId())) {
                    continue;
                }
                price = price.subtract(cart.getDiscountPrice());

                if (price.compareTo(BigInteger.ZERO) < 0) {
                    price = BigInteger.ZERO;
                }
            }

            for (CartDetailEntity cartDetail : cart.getCartDetails()) {
                price = price.add(cartDetail.getOptionPrice().multiply(BigInteger.valueOf(cartDetail.getCartDtQuantity())));
            }

            groupByCompanyAmountMap.put(cart.getCpId(), groupByCompanyAmountMap.getOrDefault(cart.getCpId(), BigInteger.ZERO).add(price));
        }

        return groupByCompanyAmountMap;
    }
}
