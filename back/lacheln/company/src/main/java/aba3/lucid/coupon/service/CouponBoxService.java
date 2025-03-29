package aba3.lucid.coupon.service;

import aba3.lucid.common.exception.ApiException;
import aba3.lucid.common.status_code.ErrorCode;
import aba3.lucid.common.validate.Validator;
import aba3.lucid.domain.coupon.convertor.CouponBoxConverter;
import aba3.lucid.domain.coupon.entity.CouponBoxEntity;
import aba3.lucid.domain.coupon.entity.CouponEntity;
import aba3.lucid.domain.coupon.enums.CouponBoxStatus;
import aba3.lucid.domain.coupon.repository.CouponBoxRepository;
import aba3.lucid.domain.product.entity.ProductEntity;
import aba3.lucid.domain.user.entity.UsersEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CouponBoxService {

    private final CouponBoxRepository couponBoxRepository;
    private final CouponService couponService;

    private final CouponBoxConverter couponBoxConverter;


    // 유저가 업체 쿠폰 등록하기
    public void claimCoupon(UsersEntity user, CouponEntity coupon) {
        // 유효기간 확인하기
        if (couponService.isNotCouponExpired(coupon)) {
            throw new ApiException(ErrorCode.GONE);
        }

        // 쿠폰을 가지고 있는지
        if (doesUserOwnCoupon(user, coupon)) {
            throw new ApiException(ErrorCode.BAD_REQUEST);
        }

        // 쿠폰 등록하기
        CouponBoxEntity couponBox = couponBoxConverter.toEntity(coupon, user);
        couponBoxRepository.save(couponBox);
    }

    // 쿠폰을 사용한 상품 가격
    public BigInteger getDiscountPrice(UsersEntity user, CouponBoxEntity couponBox, ProductEntity product) {
        CouponEntity coupon = couponBox.getCoupon();

        // 사용자가 가지고 있는 쿠폰인지
        if (!doesUserOwnCoupon(user, coupon)) {
            throw new ApiException(ErrorCode.BAD_REQUEST);
        }

        // 유효기간이 만료되지 않았는지
        if (couponService.isNotCouponExpired(coupon)) {
            throw new ApiException(ErrorCode.NULL_POINT);
        }

        // 해당 상품에 대한 쿠폰인지
        if (coupon.getProduct() != null && coupon.getProduct().equals(product)) {
            return getDiscountedPrice(product, coupon);
        }

        // 업체용 쿠폰일 때 해당 업체의 상품인지
        if (coupon.getCompany().equals(product.getCompany())) {
            // TODO 업체용일 경우 모든 상품들이 할인되므로 모든 상품 가격에서 총 가격을 할인할 때 로직에서 적용하기
            return product.getPdPrice()
                    .multiply(BigInteger.valueOf(coupon.getCouponDiscountRate()))
                    .divide(BigInteger.valueOf(100));
        }

        return product.getPdPrice();
    }

    // 쿠폰 사용 상태로 변경
    public void consumeCoupon(CouponBoxEntity couponBox) {
        couponBox.updateStatus(CouponBoxStatus.USED);
    }


    // 할인된 가격 구하기
    public BigInteger getDiscountedPrice(ProductEntity product, CouponEntity coupon) {
        // 할인 적용 대상 금액 = min(상품 가격, 쿠폰 최대 할인 가능 금액)
        BigInteger applicablePrice = product.getPdPrice().min(coupon.getCouponMaximumCost());

        // 할인 금액 = (적용 대상 금액 * 할인율) / 100
        BigInteger discountAmount = applicablePrice
                .multiply(BigInteger.valueOf(coupon.getCouponDiscountRate()))
                .divide(BigInteger.valueOf(100));

        // 최종 가격 = 원래 상품 가격 - 할인 금액
        return product.getPdPrice().subtract(discountAmount);
    }


    // 유저의 쿠폰 리스트 가지고 오기
    public List<CouponEntity> findAllByUserCouponList(UsersEntity user) {
        Validator.throwIfNull(user);

        List<CouponBoxEntity> couponBoxEntityList = couponBoxRepository.findAllByUsers_UserId(user.getUserId());

        return couponBoxEntityList.stream()
                .map(CouponBoxEntity::getCoupon)
                .toList()
                ;
    }

    // 소비자가 소유하고 있는 쿠폰인지
    public boolean doesUserOwnCoupon(UsersEntity user, CouponEntity coupon) {
        return couponBoxRepository.existsByCoupon_CouponIdAndUsers_UserId(coupon.getCouponId(), user.getUserId());
    }

    public CouponBoxEntity findByIdWithThrow(Long couponBoxId) {
        return couponBoxRepository.findById(couponBoxId)
                .orElseThrow(() -> new ApiException(ErrorCode.NULL_POINT));
    }


}
