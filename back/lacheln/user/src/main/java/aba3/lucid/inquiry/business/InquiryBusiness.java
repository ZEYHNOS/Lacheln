package aba3.lucid.inquiry.business;

import aba3.lucid.common.annotation.Business;
import aba3.lucid.common.auth.AuthUtil;
import aba3.lucid.domain.inquiry.converter.InquiryConvertor;
import aba3.lucid.domain.inquiry.dto.InquiryCreateRequest;
import aba3.lucid.domain.inquiry.dto.InquiryResponse;
import aba3.lucid.domain.inquiry.entity.InquiryEntity;
import aba3.lucid.domain.user.entity.UsersEntity;
import aba3.lucid.inquiry.service.InquiryService;
import aba3.lucid.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * InquiryBusiness
 * - 비즈니스 흐름 및 유효성 검증을 담당하는 계층
 */
@Slf4j
@Business
@RequiredArgsConstructor
public class InquiryBusiness {

    private final UserService userService;
    private final InquiryService inquiryService;

    /**
     * 문의 작성 비즈니스 로직
     * - 사용자 인증 → Entity 변환 → 저장 → 응답 DTO 반환
     * @param request 사용자 입력값
     * @return 저장된 문의 응답 DTO
     */
    public InquiryResponse createInquiry(InquiryCreateRequest request) {
        String userId = AuthUtil.getUserId();
        UsersEntity user = userService.findByIdWithThrow(userId);

        InquiryEntity inquiry = InquiryConvertor.toEntity(user, request);
        InquiryEntity saved = inquiryService.saveInquiry(inquiry);

        return InquiryConvertor.toResponse(saved);
    }

    /**
     * 로그인한 사용자의 모든 문의 내역 조회
     * @return InquiryResponse 리스트
     */
    public List<InquiryResponse> getMyInquiries() {
        String userId = AuthUtil.getUserId();
        return inquiryService.findInquiriesByUserId(userId).stream()
                .map(InquiryConvertor::toResponse)
                .toList();
    }
}
