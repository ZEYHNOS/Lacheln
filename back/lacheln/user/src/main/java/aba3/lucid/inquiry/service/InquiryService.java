package aba3.lucid.inquiry.service;

import aba3.lucid.common.exception.ApiException;
import aba3.lucid.common.status_code.ErrorCode;
import aba3.lucid.domain.inquiry.entity.InquiryEntity;
import aba3.lucid.domain.inquiry.repository.InquiryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * InquiryService
 * - DB 접근 및 트랜잭션 처리를 담당하는 계층
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class InquiryService {

    private final InquiryRepository inquiryRepository;

    @Transactional
    public InquiryEntity saveInquiry(InquiryEntity inquiry) {
        return inquiryRepository.save(inquiry);
    }

    @Transactional(readOnly = true)
    public List<InquiryEntity> findInquiriesByUserId(String userId) {
        return inquiryRepository.findByUsersUserId(userId);
    }

    @Transactional(readOnly = true)
    public InquiryEntity findByIdWithThrow(Long inquiryId) {
        return inquiryRepository.findById(inquiryId)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND));
    }
}

