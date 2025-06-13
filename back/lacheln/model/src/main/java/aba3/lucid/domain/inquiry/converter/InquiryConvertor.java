package aba3.lucid.domain.inquiry.converter;

import aba3.lucid.common.annotation.Converter;
import aba3.lucid.domain.inquiry.dto.InquiryCreateRequest;
import aba3.lucid.domain.inquiry.dto.InquiryDetailResponse;
import aba3.lucid.domain.inquiry.dto.InquiryListResponse;
import aba3.lucid.domain.inquiry.entity.InquiryEntity;
import aba3.lucid.domain.inquiry.enums.InquiryStatus;
import aba3.lucid.domain.user.entity.UsersEntity;

/**
 * InquiryConvertor
 * - Inquiry 관련 Entity ↔ DTO 간의 변환을 담당하는 클래스
 */
@Converter
public class InquiryConvertor {

    // 문의 생성용 Entity 변환
    public static InquiryEntity toEntity(UsersEntity user, InquiryCreateRequest request) {
        return InquiryEntity.builder()
                .users(user)
                .inquiryTitle(request.getInquiryTitle())
                .inquiryCategory(request.getInquiryCategory())
                .inquiryContent(request.getInquiryContent())
                .inquiryStatus(InquiryStatus.IN_PROGRESS)
                .build();
    }

    // ✅ 일반 사용자용 리스트 응답 변환
    public static InquiryListResponse toListResponseForUser(InquiryEntity inquiry) {
        return InquiryListResponse.builder()
                .inquiryId(inquiry.getInquiryId())
                .title(inquiry.getInquiryTitle())
                .status(inquiry.getInquiryStatus())
                .createdAt(inquiry.getInquiryCreatedAt())
                .userEmail(null) // 사용자용: 이메일 미포함
                .build();
    }

    // ✅ 관리자용 리스트 응답 변환
    public static InquiryListResponse toListResponseForAdmin(InquiryEntity inquiry) {
        return InquiryListResponse.builder()
                .inquiryId(inquiry.getInquiryId())
                .title(inquiry.getInquiryTitle())
                .status(inquiry.getInquiryStatus())
                .createdAt(inquiry.getInquiryCreatedAt())
                .userEmail(inquiry.getUsers().getUserEmail()) // 관리자용
                .build();
    }

    // ✅ 상세 조회 응답 (관리자/사용자 공통)
    public static InquiryDetailResponse toResponse(InquiryEntity inquiry) {
        return InquiryDetailResponse.builder()
                .inquiryId(inquiry.getInquiryId())
                .title(inquiry.getInquiryTitle())
                .category(inquiry.getInquiryCategory().name())
                .content(inquiry.getInquiryContent())
                .status(inquiry.getInquiryStatus().name())
                .answer(inquiry.getInquiryAnswer())
                .createdAt(inquiry.getInquiryCreatedAt().toString())
                .userEmail(inquiry.getUsers().getUserEmail()) // 관리자에서만 사용
                .build();
    }
}