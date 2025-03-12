package aba3.lucid.domain.coupon.repository;

import aba3.lucid.domain.coupon.entity.CompanyCouponEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyCouponRepository extends JpaRepository<CompanyCouponEntity, Long> {
}
