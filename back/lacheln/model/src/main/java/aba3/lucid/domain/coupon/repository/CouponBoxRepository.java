package aba3.lucid.domain.coupon.repository;

import aba3.lucid.domain.coupon.entity.CouponBoxEntity;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CouponBoxRepository extends JpaRepository<CouponBoxEntity, Long> {

    boolean existsByCoupon_CouponIdAndUsers_UserId(String couponId, String userId);

    CouponBoxEntity findByCoupon_CouponIdAndUsers_UserId(String couponId, String userId);

    List<CouponBoxEntity> findAllByUsers_UserId(String userId);

}