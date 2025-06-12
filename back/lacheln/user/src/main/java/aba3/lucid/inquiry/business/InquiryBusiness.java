package aba3.lucid.inquiry.business;

import aba3.lucid.common.annotation.Business;
import aba3.lucid.common.auth.AuthUtil;
import aba3.lucid.common.exception.ApiException;
import aba3.lucid.common.status_code.ErrorCode;
import aba3.lucid.domain.inquiry.converter.InquiryConvertor;
import aba3.lucid.domain.inquiry.dto.InquiryCreateRequest;
import aba3.lucid.domain.inquiry.dto.InquiryDetailResponse;
import aba3.lucid.domain.inquiry.dto.InquiryListResponse;
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

    // 문의 작성
    public InquiryDetailResponse createInquiry(InquiryCreateRequest request) {
        String userId = AuthUtil.getUserId();
        UsersEntity user = userService.findByIdWithThrow(userId);
        InquiryEntity inquiry = InquiryConvertor.toEntity(user, request);
        InquiryEntity saved = inquiryService.saveInquiry(inquiry);
        return InquiryConvertor.toResponse(saved);
    }

    // 문의 목록 조회
    public List<InquiryListResponse> getMyInquiries() {
        String userId = AuthUtil.getUserId();
        log.info("📥 현재 로그인한 사용자 ID: {}", userId); // ✅ 로그 추가
        List<InquiryEntity> list = inquiryService.findInquiriesByUserId(userId);
        log.info("📦 해당 사용자의 문의 개수: {}", list.size()); // ✅ 로그 추가
        return list.stream().map(InquiryConvertor::toListResponse).toList();
    }

    // 문의 상세 조회
    public InquiryDetailResponse getMyInquiryDetail(Long inquiryId) {
        String userId = AuthUtil.getUserId();
        InquiryEntity inquiry = inquiryService.findByIdWithThrow(inquiryId);

        if (!inquiry.getUsers().getUserId().equals(userId)) {
            throw new ApiException(ErrorCode.FORBIDDEN);
        }

        return InquiryConvertor.toResponse(inquiry);
    }
}
