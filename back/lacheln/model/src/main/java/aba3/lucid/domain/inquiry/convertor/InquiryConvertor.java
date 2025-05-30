package aba3.lucid.domain.inquiry.convertor;

import aba3.lucid.common.annotation.Converter;
import aba3.lucid.domain.inquiry.dto.InquiryCreateRequest;
import aba3.lucid.domain.inquiry.dto.InquiryResponse;
import aba3.lucid.domain.inquiry.entity.InquiryEntity;
import aba3.lucid.domain.inquiry.entity.InquiryImageEntity;
import aba3.lucid.domain.inquiry.enums.InquiryStatus;
import aba3.lucid.domain.user.entity.UsersEntity;

import java.util.List;
import java.util.stream.Collectors;

/**
 * InquiryEntity <-> DTO 변환기
 * - Entity 생성
 * - 응답용 DTO로 변환
 */
@Converter
public class InquiryConvertor {

    /**
     * 문의 작성 요청 DTO → 문의 엔티티로 변환
     *
     * @param user 현재 로그인한 사용자
     * @param request 문의 작성 요청 DTO
     * @return InquiryEntity
     */
    public static InquiryEntity toEntity(UsersEntity user, InquiryCreateRequest request) {
        return InquiryEntity.builder()
                .users(user)
                .inquiryTitle(request.getInquiryTitle())
                .inquiryCategory(request.getInquiryCategory())
                .inquiryContent(request.getInquiryContent())
                .inquiryStatus(InquiryStatus.RECEIVED) // 기본 상태: 접수
                .build();
    }

    /**
     * 문의 Entity → 응답용 DTO로 변환
     *
     * @param inquiry 문의 엔티티
     * @return InquiryResponse
     */
    public static InquiryResponse toResponse(InquiryEntity inquiry) {
        List<String> imageUrls = inquiry.getInquiryImageList() != null
                ? inquiry.getInquiryImageList().stream()
                .map(InquiryImageEntity::getImageUrl)
                .collect(Collectors.toList())
                : List.of();

        return InquiryResponse.builder()
                .inquiryId(inquiry.getInquiryId())
                .title(inquiry.getInquiryTitle())
                .category(inquiry.getInquiryCategory())
                .content(inquiry.getInquiryContent())
                .status(inquiry.getInquiryStatus())
                .imageUrls(imageUrls)
                .createdAt(inquiry.getInquiryCreatedAt())
                .build();
    }
}
