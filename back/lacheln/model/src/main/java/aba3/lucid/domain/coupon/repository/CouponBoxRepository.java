package aba3.lucid.domain.coupon.repository;

import aba3.lucid.domain.coupon.entity.CouponBoxEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CouponBoxRepository extends JpaRepository<CouponBoxEntity, Long> {

    boolean existsByCoupon_CouponIdAndUserId(String couponId, String userId);

    List<CouponBoxEntity> findAllByUserId(String userId);

}