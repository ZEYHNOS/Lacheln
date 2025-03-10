package aba3.lucid.repository.coupon;

import aba3.lucid.domain.coupon.CompanyCouponEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyCouponRepository extends JpaRepository<CompanyCouponEntity, Long> {
}
