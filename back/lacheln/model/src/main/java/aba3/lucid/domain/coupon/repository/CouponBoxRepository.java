package aba3.lucid.domain.coupon.repository;

import aba3.lucid.domain.coupon.entity.CouponBoxEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CouponBoxRepository extends JpaRepository<CouponBoxEntity, Long> {
}