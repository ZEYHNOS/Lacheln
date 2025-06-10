package aba3.lucid.payment.business;

import aba3.lucid.cart.business.CartBusiness;
import aba3.lucid.cart.service.CartService;
import aba3.lucid.common.annotation.Business;
import aba3.lucid.common.exception.ApiException;
import aba3.lucid.common.status_code.PaymentErrorCode;
import aba3.lucid.common.validate.Validator;
import aba3.lucid.domain.cart.dto.CartAllResponse;
import aba3.lucid.domain.cart.entity.CartEntity;
import aba3.lucid.domain.company.enums.CompanyCategory;
import aba3.lucid.domain.payment.converter.PayDetailConverter;
import aba3.lucid.domain.payment.converter.PaymentConvertor;
import aba3.lucid.domain.payment.dto.*;
import aba3.lucid.domain.payment.entity.PayDetailEntity;
import aba3.lucid.domain.payment.entity.PayDetailOptionEntity;
import aba3.lucid.domain.payment.entity.PayManagementEntity;
import aba3.lucid.domain.product.enums.DressSize;
import aba3.lucid.domain.user.entity.UsersEntity;
import aba3.lucid.payment.service.PayDetailService;
import aba3.lucid.payment.service.PaymentService;
import aba3.lucid.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Business
@RequiredArgsConstructor
public class PaymentBusiness {

    private final RabbitTemplate rabbitTemplate;

    private final PaymentConvertor paymentConvertor;
    private final PayDetailConverter payDetailConverter;

    private final PaymentService paymentService;
    private final PayDetailService payDetailService;
    private final UserService userService;
    private final CartBusiness cartBusiness;

    // 결제 정보 저장하기
    public PayManagementResponse save(String userId, PaymentRequest request) {
        Validator.throwIfNull(userId, request);

        UsersEntity user = userService.findByIdWithThrow(userId);
        List<CartAllResponse> cartAllResponseList = cartBusiness.getCarts(request.getCartIdList());
        PayManagementEntity payManagement = paymentConvertor.toEntity(request, user, cartAllResponseList);
        log.info("payManagement : {}", payManagement.getPayId());

        PayManagementEntity savedPayManagement = paymentService.save(payManagement, request.getCartIdList());

        return paymentConvertor.toResponse(savedPayManagement);
    }

    // Merchant ID 발급하기
    public String getMerchantUid() {
        return paymentService.generateMerchantUid();
    }

    // 결제 전 검증 로직 및 총 금액
    public BigInteger verificationAndGetTotalAmount(PaymentVerifyRequest request, String userId) {
        Validator.throwIfNull(request, userId);

        log.info("request : {}", request);
        // 장바구니 ID 한 개도 없을 때
        if (request.getCartIdList().isEmpty()) {
            throw new ApiException(PaymentErrorCode.NO_PRODUCT_FOR_PAYMENT);
        }

        BigInteger totalAmount = paymentService.verificationAndGetTotalAmount(request, userId);

        return totalAmount;
    }

    // 유저 결제 내역 리스트 가지고 오기
    public List<PayManagementResponse> getUserPaymentList(String userId) {
        Validator.throwIfNull(userId);

        List<PayManagementEntity> payManagementEntityList = paymentService.getUserPaymentList(userId);

        return paymentConvertor.toResponseList(payManagementEntityList);
    }

    // 업체 결제 내역 리스트 가지고 오기
    public List<PayDetailResponse> getPaymentList(Long companyId) {
        Validator.throwIfNull(companyId);

        List<PayDetailEntity> payManagementEntityList = payDetailService.getPayDetailList(companyId);

        return payDetailConverter.toResponseList(payManagementEntityList);
    }

    // 유저 결제 내역 리스트 가지고 오기
    public List<PayDetailResponse> getPaymentList(String userId) {

        UsersEntity user = userService.findByIdWithThrow(userId);

        List<PayDetailEntity> payDetailEntityList = payDetailService.getPayDetailList(user);

        return payDetailConverter.toResponseList(payDetailEntityList);
    }
    
    // 상품 ID 및 날짜로 리스트 가지고 오기
    public List<PayDetailBlockResponse> getPaymentPdIdAndDate(Long pdId, LocalDate date) {
        Validator.throwIfNull(pdId, date);

        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = start.plusDays(1);

        List<PayDetailEntity> payManagementEntityList = payDetailService.getPayDetailList(pdId, start, end);
        List<PayDetailBlockResponse> responses = new ArrayList<>();
        Map<Long, PayDetailBlockResponse.DressDetail> map = new HashMap<>();

        for(PayDetailEntity entity : payManagementEntityList) {
            if(entity.getCategory() == CompanyCategory.D)   {
                for(PayDetailOptionEntity option : entity.getPayDetailOptionEntityList())   {
                    map.put(option.getPayDetailOptionId(), PayDetailBlockResponse.DressDetail.builder()
                            .dressQuantity(option.getPayDtQuantity())
                            .startTime(entity.getStartDatetime())
                            .endTime(entity.getEndDatetime())
                            .dressSize(option.getDressSize())
                            .build());
                }
                responses.add(PayDetailBlockResponse.builder()
                        .dressQuantity(map)
                        .build());
            } else if(entity.getCategory() == CompanyCategory.M)    {
                responses.add(PayDetailBlockResponse.builder()
                        .pdId(entity.getPdId())
                        .startTime(entity.getStartDatetime())
                        .endTime(entity.getEndDatetime())
                        .managerName(entity.getManager())
                        .build());
            } else {
                responses.add(PayDetailBlockResponse.builder()
                        .pdId(entity.getPdId())
                        .startTime(entity.getStartDatetime())
                        .endTime(entity.getEndDatetime())
                        .build());
            }
        }

        return payDetailConverter.toBlockResponseList(payManagementEntityList);
    }
}
