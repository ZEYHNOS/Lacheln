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
 * InquiryService
 * - 문의 관련 트랜잭션 처리를 담당하는 서비스 계층 클래스
 * - 실질적인 로직 처리는 Business 계층에서 수행하며,
 *   Service는 비즈니스 계층을 호출하고 트랜잭션만 관리함
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class InquiryService {

    // 비즈니스 로직 담당 클래스 (실제 처리 책임은 Business로 위임)
    private final InquiryBusiness inquiryBusiness;

    /**
     * 문의 작성 트랜잭션 처리 메서드
     * - 사용자가 작성한 문의 데이터를 저장하기 위한 트랜잭션을 시작함
     * - 비즈니스 계층을 호출하여 유효성 검사 및 엔티티 생성, 저장 등을 수행
     *
     * @param userId  현재 로그인한 사용자의 UUID
     * @param request 문의 작성 요청 DTO (제목, 카테고리, 내용, 이미지 URL 포함)
     */
    @Transactional
    public void createInquiry(String userId, InquiryCreateRequest request) {
        inquiryBusiness.createInquiry(userId, request);
    }

    /**
     * 사용자 문의 목록 조회 트랜잭션 처리 메서드
     * - 로그인한 사용자가 작성한 모든 문의 내역을 조회함
     * - 읽기 전용 트랜잭션(readOnly = true)으로 성능 최적화 적용
     *
     * @param userId 현재 로그인한 사용자의 UUID
     * @return List<InquiryResponse> 사용자가 작성한 문의 DTO 리스트
     */
    @Transactional(readOnly = true)
    public List<InquiryResponse> getMyInquiries(String userId) {
        return inquiryBusiness.getMyInquiries(userId);
    }
}