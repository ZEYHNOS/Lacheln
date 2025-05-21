package aba3.lucid.domain.inquiry.repository;

import aba3.lucid.domain.inquiry.entity.InquiryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InquiryRepository extends JpaRepository<InquiryEntity, Long> {

    /**
     * 특정 사용자(userId)가 작성한 문의 목록을 조회하는 메서드
     *
     * @param userId 현재 로그인한 사용자의 UUID
     * @return 해당 사용자의 모든 문의 리스트
     */
    List<InquiryEntity> findByUsersUserId(String userId);
}