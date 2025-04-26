package aba3.lucid.payment.service;

import aba3.lucid.common.exception.ApiException;
import aba3.lucid.common.status_code.ErrorCode;
import aba3.lucid.domain.payment.entity.PayManagementEntity;
import aba3.lucid.domain.payment.repository.PayManagementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PayManagementRepository payManagementRepository;

    public PayManagementEntity save(PayManagementEntity entity) {
        return payManagementRepository.save(entity);
    }

    public PayManagementEntity refund(String id) {
        PayManagementEntity entity = findByIdWithThrow(id);

        entity.refund();
        // TODO 환불 로직 작성하기

        return payManagementRepository.save(entity);
    }


    public PayManagementEntity findByIdWithThrow(String id) {
        return payManagementRepository.findById(id)
                .orElseThrow(() -> new ApiException(ErrorCode.BAD_REQUEST));
    }

}
