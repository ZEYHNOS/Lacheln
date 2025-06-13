package aba3.lucid.domain.inquiry.repository;

import aba3.lucid.domain.inquiry.entity.InquiryEntity;
import aba3.lucid.domain.inquiry.enums.InquiryStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InquiryRepository extends JpaRepository<InquiryEntity, Long> {

    /**
     * 특정 사용자(userId)가 작성한 문의 목록 조회
     */
    List<InquiryEntity> findByUsersUserId(String userId);

    /**
     * 문의 상태(IN_PROGRESS, COMPLETED 등)별 개수 조회
     */
    int countByInquiryStatus(InquiryStatus status);
}
