package aba3.lucid.inquiry.business;

import aba3.lucid.common.annotation.Business;
import aba3.lucid.domain.inquiry.convertor.InquiryConvertor;
import aba3.lucid.domain.inquiry.dto.InquiryCreateRequest;
import aba3.lucid.domain.inquiry.dto.InquiryResponse;
import aba3.lucid.domain.inquiry.entity.InquiryEntity;
import aba3.lucid.domain.inquiry.entity.InquiryImageEntity;
import aba3.lucid.domain.inquiry.repository.InquiryImageRepository;
import aba3.lucid.domain.inquiry.repository.InquiryRepository;
import aba3.lucid.domain.user.entity.UsersEntity;
import aba3.lucid.domain.user.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * InquiryBusiness
 * - 문의 등록 및 조회에 대한 비즈니스 로직을 담당하는 클래스
 */
@Business
@RequiredArgsConstructor
public class InquiryBusiness {

    private final InquiryRepository inquiryRepository;
    private final InquiryImageRepository inquiryImageRepository;
    private final UsersRepository usersRepository;

    /**
     * 문의 등록 처리
     *
     * @param userId 현재 로그인한 사용자 UUID
     * @param request 문의 작성 요청 DTO
     */
    @Transactional
    public void createInquiry(String userId, InquiryCreateRequest request) {
        // 🔍 1. 사용자 정보 조회 (기존 오류 수정 부분)
        UsersEntity user = usersRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자 정보를 찾을 수 없습니다."));

        // 📝 2. 문의 엔티티 생성 및 저장
        InquiryEntity inquiry = InquiryConvertor.toEntity(user, request);
        inquiryRepository.save(inquiry);

        // 🖼️ 3. 이미지가 있는 경우 이미지 엔티티로 변환 후 저장
        if (request.getImageUrls() != null && !request.getImageUrls().isEmpty()) {
            List<InquiryImageEntity> imageList = request.getImageUrls().stream()
                    .map(url -> InquiryImageEntity.builder()
                            .inquiry(inquiry)
                            .imageUrl(url)
                            .build())
                    .toList();
            inquiryImageRepository.saveAll(imageList);
        }
    }

    /**
     * 내가 작성한 문의 목록 조회
     *
     * @param userId 현재 로그인한 사용자 UUID
     * @return InquiryResponse 리스트 (문의 내역)
     */
    @Transactional(readOnly = true)
    public List<InquiryResponse> getMyInquiries(String userId) {
        List<InquiryEntity> myInquiries = inquiryRepository.findByUsersUserId(userId);
        return myInquiries.stream()
                .map(InquiryConvertor::toResponse)
                .toList();
    }
}