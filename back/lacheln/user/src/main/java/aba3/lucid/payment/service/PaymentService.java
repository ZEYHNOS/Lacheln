package aba3.lucid.payment.service;

import aba3.lucid.common.exception.ApiException;
import aba3.lucid.common.status_code.ErrorCode;
import aba3.lucid.domain.payment.entity.PayManagementEntity;
import aba3.lucid.domain.payment.repository.PayManagementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentService {

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

}
