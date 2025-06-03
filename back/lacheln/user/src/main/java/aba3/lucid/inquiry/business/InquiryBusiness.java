package aba3.lucid.inquiry.business;

import aba3.lucid.common.annotation.Business;
import aba3.lucid.domain.inquiry.converter.InquiryConvertor;
import aba3.lucid.domain.inquiry.dto.InquiryCreateRequest;
import aba3.lucid.domain.inquiry.dto.InquiryResponse;
import aba3.lucid.domain.inquiry.entity.InquiryEntity;
import aba3.lucid.domain.inquiry.entity.InquiryImageEntity;
import aba3.lucid.domain.inquiry.repository.InquiryImageRepository;
import aba3.lucid.domain.inquiry.repository.InquiryRepository;
import aba3.lucid.domain.user.entity.UsersEntity;
import aba3.lucid.domain.user.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * InquiryBusiness
 * - 문의 작성 및 조회 기능의 핵심 비즈니스 로직을 담당하는 클래스
 * - 유효성 검사, 엔티티 변환, 흐름 제어 등의 실제 로직 수행
 * - 트랜잭션은 Service 계층에서 처리하며, Business는 순수한 로직에 집중
 */
@Slf4j
@Business
@RequiredArgsConstructor
public class InquiryBusiness {

    private final InquiryRepository inquiryRepository;
    private final InquiryImageRepository inquiryImageRepository;
    private final UsersRepository usersRepository;

    /**
     * 문의 작성 비즈니스 로직
     *
     * - 사용자 정보를 조회하고, 유효하지 않으면 예외 발생
     * - InquiryEntity를 생성 후 저장
     * - 이미지 URL 리스트가 있을 경우 InquiryImageEntity로 변환 후 일괄 저장
     *
     * @param userId 현재 로그인한 사용자 UUID
     * @param request 문의 작성 요청 DTO (제목, 카테고리, 내용, 이미지 리스트 포함)
     * @throws IllegalArgumentException 사용자가 존재하지 않을 경우 예외 발생
     */
    public void createInquiry(String userId, InquiryCreateRequest request) {
        // (1) 사용자 유효성 검사
        UsersEntity user = usersRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자 정보를 찾을 수 없습니다."));

        // (2) InquiryEntity 생성 (상태: RECEIVED)
        InquiryEntity inquiry = InquiryConvertor.toEntity(user, request);
        inquiryRepository.save(inquiry);

        // (3) 이미지가 존재하는 경우 이미지 엔티티로 변환하여 저장
        if (request.getImageUrls() != null && !request.getImageUrls().isEmpty()) {
            List<InquiryImageEntity> imageList = request.getImageUrls().stream()
                    .map(url -> InquiryImageEntity.builder()
                            .inquiry(inquiry)        // 문의와 연관관계 설정
                            .imageUrl(url)           // 이미지 URL 저장
                            .build())
                    .toList();

            // 일괄 저장 (cascade 아님에 주의)
            inquiryImageRepository.saveAll(imageList);
        }
    }

    /**
     * 특정 사용자의 문의 내역 전체 조회 로직
     *
     * - 해당 사용자가 작성한 모든 문의 엔티티를 조회
     * - 각 문의를 InquiryResponse DTO로 변환하여 응답 리스트 생성
     *
     * @param userId 현재 로그인한 사용자 UUID
     * @return 사용자가 작성한 모든 문의 목록 (DTO 형태)
     */
    public List<InquiryResponse> getMyInquiries(String userId) {
        // (1) 사용자 기준으로 문의 목록 조회
        List<InquiryEntity> myInquiries = inquiryRepository.findByUsersUserId(userId);

        // (2) DTO 변환 후 반환
        return myInquiries.stream()
                .map(InquiryConvertor::toResponse)
                .toList();
    }
}
