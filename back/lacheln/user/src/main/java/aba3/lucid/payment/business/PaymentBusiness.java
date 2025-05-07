package aba3.lucid.payment.business;

import aba3.lucid.common.annotation.Business;
import aba3.lucid.common.exception.ApiException;
import aba3.lucid.common.status_code.PaymentErrorCode;
import aba3.lucid.common.validate.Validator;
import aba3.lucid.domain.payment.convertor.PayDetailConverter;
import aba3.lucid.domain.payment.convertor.PaymentConvertor;
import aba3.lucid.domain.payment.dto.PayDetailResponse;
import aba3.lucid.domain.payment.dto.PayManagementResponse;
import aba3.lucid.domain.payment.dto.PaymentRequest;
import aba3.lucid.domain.payment.dto.PaymentVerifyRequest;
import aba3.lucid.domain.payment.entity.PayDetailEntity;
import aba3.lucid.domain.payment.entity.PayManagementEntity;
import aba3.lucid.domain.user.entity.UsersEntity;
import aba3.lucid.payment.service.PayDetailService;
import aba3.lucid.payment.service.PaymentService;
import aba3.lucid.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.math.BigInteger;
import java.util.List;

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

    // 결제 정보 저장하기
    public PayManagementResponse save(String userId, PaymentRequest request) {
        Validator.throwIfNull(userId, request);

        UsersEntity user = userService.findByIdWithThrow(userId);
        PayManagementEntity payManagement = paymentConvertor.toEntity(request, user);

        PayManagementEntity savedPayManagement = paymentService.save(payManagement);

        return paymentConvertor.toResponse(savedPayManagement);
    }


    public String getMerchantUid() {
        return paymentService.generateMerchantUid();
    }

    // 결제 전 검증 로직 및 총 금액
    public BigInteger verificationAndGetTotalAmount(PaymentVerifyRequest request, String userId) {
        Validator.throwIfNull(request, userId);

        // 장바구니 ID 한 개도 없을 때
        if (request.getCardIdList().isEmpty()) {
            throw new ApiException(PaymentErrorCode.NO_PRODUCT_FOR_PAYMENT);
        }

        BigInteger totalAmount = paymentService.verificationAndGetTotalAmount(request, userId);

        return totalAmount;
    }

    // 결제 내역 리스트 가지고 오기
    public List<PayManagementResponse> getPaymentList(String userId) {
        Validator.throwIfNull(userId);

        List<PayManagementEntity> payManagementEntityList = paymentService.getPaymentList(userId);

        return paymentConvertor.toResponseList(payManagementEntityList);
    }

    // 결제 내역 리스트 가지고 오기
    public List<PayDetailResponse> getPaymentList(Long companyId) {
        Validator.throwIfNull(companyId);

        List<PayDetailEntity> payManagementEntityList = payDetailService.getPayDetailList(companyId);

        return payDetailConverter.toResponseList(payManagementEntityList);
    }
}
