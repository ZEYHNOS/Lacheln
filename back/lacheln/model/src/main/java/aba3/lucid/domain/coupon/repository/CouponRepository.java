package aba3.lucid.domain.coupon.repository;

import aba3.lucid.domain.coupon.entity.CouponEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponRepository extends JpaRepository<CouponEntity, String> {
}
