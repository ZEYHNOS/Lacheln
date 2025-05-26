package aba3.lucid.inquiry.service;

import aba3.lucid.domain.inquiry.dto.InquiryCreateRequest;
import aba3.lucid.domain.inquiry.dto.InquiryResponse;
import aba3.lucid.inquiry.business.InquiryBusiness;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 문의 서비스 계층
 * - 트랜잭션 관리 + 비즈니스 호출 담당
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class InquiryService {

    private final InquiryBusiness inquiryBusiness;

    @Transactional
    public void createInquiry(String userId, InquiryCreateRequest request) {
        inquiryBusiness.createInquiry(userId, request);
    }

    @Transactional(readOnly = true)
    public List<InquiryResponse> getMyInquiries(String userId) {
        return inquiryBusiness.getMyInquiries(userId);
    }
}

