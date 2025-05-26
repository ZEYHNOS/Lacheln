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
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * InquiryBusiness
 * - 문의 등록 및 조회에 대한 비즈니스 로직을 담당하는 클래스
 */
@Slf4j
@Business
@RequiredArgsConstructor
public class InquiryBusiness {

    private final InquiryRepository inquiryRepository;
    private final InquiryImageRepository inquiryImageRepository;
    private final UsersRepository usersRepository;

    /**
     * 문의 등록 처리
     */
    public void createInquiry(String userId, InquiryCreateRequest request) {
        UsersEntity user = usersRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자 정보를 찾을 수 없습니다."));

        InquiryEntity inquiry = InquiryConvertor.toEntity(user, request);
        inquiryRepository.save(inquiry);

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
     * 사용자 문의 내역 조회
     */
    public List<InquiryResponse> getMyInquiries(String userId) {
        List<InquiryEntity> myInquiries = inquiryRepository.findByUsersUserId(userId);
        return myInquiries.stream()
                .map(InquiryConvertor::toResponse)
                .toList();
    }
}
