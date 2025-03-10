package aba3.lucid.repository.coupon;

import aba3.lucid.domain.coupon.CouponEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponRepository extends JpaRepository<CouponEntity, String> {
}
