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

    /**
     * InquiryCreateRequest → InquiryEntity 변환
     * @param user 현재 로그인한 사용자
     * @param request 사용자가 입력한 문의 작성 요청 DTO
     * @return DB 저장용 InquiryEntity 객체
     */
    public static InquiryEntity toEntity(UsersEntity user, InquiryCreateRequest request) {
        return InquiryEntity.builder()
                .users(user)
                .inquiryTitle(request.getInquiryTitle())
                .inquiryCategory(request.getInquiryCategory())
                .inquiryContent(request.getInquiryContent())
                .inquiryStatus(InquiryStatus.IN_PROGRESS)  // 기본 상태는 '접수'
                .build();
    }

    /**
     * 내역 리스트 조회
     */
    public static InquiryListResponse toListResponse(InquiryEntity inquiry) {
        return InquiryListResponse.builder()
                .inquiryId(inquiry.getInquiryId())
                .title(inquiry.getInquiryTitle())
                .status(inquiry.getInquiryStatus())
                .createdAt(inquiry.getInquiryCreatedAt())
                .build();
    }

    /**
     * InquiryEntity → InquiryResponse 변환
     * 내역 상세 조회
     * @param inquiry DB에 저장된 문의 엔티티
     * @return 클라이언트 응답용 DTO
     */
    public static InquiryDetailResponse toResponse(InquiryEntity inquiry) {
        return InquiryDetailResponse.builder()
                .inquiryId(inquiry.getInquiryId())
                .title(inquiry.getInquiryTitle())
                .category(inquiry.getInquiryCategory())
                .content(inquiry.getInquiryContent())
                .status(inquiry.getInquiryStatus())
                .createdAt(inquiry.getInquiryCreatedAt())
                .build();
    }
}
