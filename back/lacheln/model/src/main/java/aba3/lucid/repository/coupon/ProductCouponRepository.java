package aba3.lucid.repository.coupon;

import aba3.lucid.domain.coupon.ProductCouponEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductCouponRepository extends JpaRepository<ProductCouponEntity, Long> {
}
