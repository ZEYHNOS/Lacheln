package aba3.lucid.domain.inquiry.repository;

import aba3.lucid.domain.inquiry.entity.InquiryImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InquiryImageRepository extends JpaRepository<InquiryImageEntity, Long> {
}
