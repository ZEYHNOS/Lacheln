package aba3.lucid.coupon.service;

import aba3.lucid.common.exception.ApiException;
import aba3.lucid.common.status_code.CouponErrorCode;
import aba3.lucid.common.validate.Validator;
import aba3.lucid.domain.coupon.convertor.CouponBoxConverter;
import aba3.lucid.domain.coupon.entity.CouponBoxEntity;
import aba3.lucid.domain.coupon.entity.CouponEntity;
import aba3.lucid.domain.coupon.enums.CouponBoxStatus;
import aba3.lucid.domain.coupon.repository.CouponBoxRepository;
import aba3.lucid.domain.product.entity.ProductEntity;
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

    private final CouponBoxConverter couponBoxConverter;

    private final CouponVerificationService couponVerificationService;

    // 유저가 소유한 쿠폰 전부 가져오기
    public List<CouponBoxEntity> getAllCouponBox(String userId) {
        return couponBoxRepository.findAllByUserId(userId);
    }

    // 유저가 업체 쿠폰 등록하기
    public void claimCoupon(String userId, CouponEntity coupon) {
        // 유효기간 확인하기
        couponVerificationService.throwIfCouponExpired(coupon);

        // 쿠폰을 가지고 있는지
        throwIfNotCouponOwnerByCoupon(userId, coupon);

        // 쿠폰 등록하기
        CouponBoxEntity couponBox = couponBoxConverter.toEntity(coupon, userId);
        couponBoxRepository.save(couponBox);
    }

    // 쿠폰을 사용한 상품 가격
    public BigInteger getDiscountPrice(String userId, CouponBoxEntity couponBox, ProductEntity product) {
        CouponEntity coupon = couponBox.getCoupon();

        // 사용자가 가지고 있는 쿠폰인지
        throwIfNotCouponOwnerByCoupon(userId, coupon);

        // 유효기간이 만료되지 않았는지
        couponVerificationService.throwIfCouponExpired(coupon);

        // TODO 상품 리스트로 오니깐 상품 그룹을 만들어서 총액 계산 하기


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
    public List<CouponEntity> findAllByUserCouponList(String userId) {
        Validator.throwIfNull(userId);

        List<CouponBoxEntity> couponBoxEntityList = couponBoxRepository.findAllByUserId(userId);

        return couponBoxEntityList.stream()
                .map(CouponBoxEntity::getCoupon)
                .toList()
                ;
    }

    // 소비자가 소유하고 있는 쿠폰인지
    public void throwIfNotCouponOwnerByCoupon(String userId, CouponEntity coupon) {
        if (!couponBoxRepository.existsByCoupon_CouponIdAndUserId(coupon.getCouponId(), userId)) {
            throw new ApiException(CouponErrorCode.NO_COUPON_OWNERSHIP);
        }
    }

    // 소비자가 소유하고 있는 쿠폰 리스트인지
    public void throwIfNotCouponOwnerByCoupon(String userId, List<CouponEntity> couponList) {
        for (CouponEntity coupon : couponList) {
            throwIfNotCouponOwnerByCoupon(userId, coupon);
        }
    }

    public void throwIfNotCouponOwnerByBox(String userId, List<CouponBoxEntity> couponBoxEntityList) {
        for (CouponBoxEntity couponBox : couponBoxEntityList) {
            if (!couponBox.getUserId().equals(userId)) {
                throw new ApiException(CouponErrorCode.NO_COUPON_OWNERSHIP);
            }
        }
    }

    public List<CouponBoxEntity> findAllById(List<Long> couponBoxIdList) {
        return couponBoxRepository.findAllById(couponBoxIdList);
    }

    public CouponBoxEntity findByIdWithThrow(Long couponBoxId) {
        return couponBoxRepository.findById(couponBoxId)
                .orElseThrow(() -> new ApiException(CouponErrorCode.COUPON_NOT_FOUND));
    }



}
