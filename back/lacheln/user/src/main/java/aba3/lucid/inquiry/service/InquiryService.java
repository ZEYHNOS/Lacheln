package aba3.lucid.inquiry.service;

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

    /**
     * 문의 저장 (INSERT)
     * @param inquiry 저장할 문의 엔티티
     * @return 저장된 엔티티 반환
     */
    @Transactional
    public InquiryEntity saveInquiry(InquiryEntity inquiry) {
        return inquiryRepository.save(inquiry);
    }

    /**
     * 특정 사용자의 모든 문의 목록 조회 (SELECT)
     * @param userId 로그인한 사용자 UUID
     * @return InquiryEntity 리스트
     */
    @Transactional(readOnly = true)
    public List<InquiryEntity> findInquiriesByUserId(String userId) {
        return inquiryRepository.findByUsersUserId(userId);
    }
}
