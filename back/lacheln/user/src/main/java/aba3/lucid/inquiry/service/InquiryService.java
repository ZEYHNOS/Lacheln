package aba3.lucid.inquiry.service;

import aba3.lucid.common.exception.ApiException;
import aba3.lucid.common.status_code.ErrorCode;
import aba3.lucid.domain.inquiry.entity.InquiryEntity;
import aba3.lucid.domain.inquiry.enums.InquiryStatus;
import aba3.lucid.domain.inquiry.repository.InquiryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class InquiryService {

    private final InquiryRepository inquiryRepository;

    // ✅ 일반 사용자 - 내 문의 저장
    @Transactional
    public InquiryEntity saveInquiry(InquiryEntity inquiry) {
        return inquiryRepository.save(inquiry);
    }

    // ✅ 일반 사용자 - 내 문의 목록 조회
    @Transactional(readOnly = true)
    public List<InquiryEntity> findInquiriesByUserId(String userId) {
        return inquiryRepository.findByUsersUserId(userId);
    }

    // ✅ 상세 조회 (관리자/사용자 공통)
    @Transactional(readOnly = true)
    public InquiryEntity findByIdWithThrow(Long inquiryId) {
        return inquiryRepository.findById(inquiryId)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND));
    }

    // ✅ 관리자 - 전체 문의 목록 조회
    @Transactional(readOnly = true)
    public List<InquiryEntity> findAllInquiries() {
        return inquiryRepository.findAll();
    }

    // ✅ 관리자 - 상태별 문의 개수 조회
    @Transactional(readOnly = true)
    public int countByStatus(InquiryStatus status) {
        return inquiryRepository.countByInquiryStatus(status);
    }

    // ✅ 관리자 - 문의 답변 등록
    @Transactional
    public void answerInquiry(Long inquiryId, String answer) {
        InquiryEntity inquiry = findByIdWithThrow(inquiryId);
        inquiry.setInquiryAnswer(answer);
        inquiry.setInquiryStatus(InquiryStatus.COMPLETED);
    }
}