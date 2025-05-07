package aba3.lucid.payment.service;

import aba3.lucid.domain.payment.entity.PayDetailEntity;
import aba3.lucid.domain.payment.repository.PayDetailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PayDetailService {

    private final PayDetailRepository payDetailRepository;

    public List<PayDetailEntity> getPayDetailList(Long companyId) {
        return payDetailRepository.findAllByCpId(companyId);
    }

}
