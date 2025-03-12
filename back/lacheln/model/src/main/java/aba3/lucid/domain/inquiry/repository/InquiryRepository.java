package aba3.lucid.domain.inquiry.repository;

import aba3.lucid.domain.inquiry.entity.InquiryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InquiryRepository extends JpaRepository<InquiryEntity, Long> {
}
