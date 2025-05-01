package aba3.lucid.coupon.service;

import aba3.lucid.common.exception.ApiException;
import aba3.lucid.common.status_code.CouponErrorCode;
import aba3.lucid.common.status_code.ErrorCode;
import aba3.lucid.domain.company.entity.CompanyEntity;
import aba3.lucid.domain.coupon.entity.CouponBoxEntity;
import aba3.lucid.domain.coupon.entity.CouponEntity;
import aba3.lucid.domain.coupon.enums.CouponBoxStatus;
import aba3.lucid.domain.product.entity.ProductEntity;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class CouponVerificationService {

    // 만료기간이 지난 쿠폰인지
    public void throwIfCouponExpired(CouponEntity coupon) {
        if (coupon.getCouponExpirationDate().isBefore(LocalDateTime.now())) {
            throw new ApiException(CouponErrorCode.EXPIRED_COUPON);
        }
    }

    // 만료기간이 지난 쿠폰인지
    public void throwIfCouponExpired(List<CouponEntity> couponList) {
        for (CouponEntity coupon : couponList) {
            throwIfCouponExpired(coupon);
        }
    }

    // 해당 쿠폰을 사용했는지
    public void throwIfCouponUsedOrExpired(CouponBoxEntity couponBox) {
        CouponBoxStatus status = couponBox.getCouponStatus();
        if (!status.equals(CouponBoxStatus.UNUSED)) {
            throw new ApiException(CouponErrorCode.USED_COUPON);
        }
    }

    // 해당 쿠폰을 사용했는지
    public void throwIfCouponUsedOrExpired(List<CouponBoxEntity> couponBoxList) {
        for (CouponBoxEntity couponBox : couponBoxList) {
            throwIfCouponUsedOrExpired(couponBox);
        }
    }

    // 사용하는 쿠폰이 결제하려는 상품과 무관한 쿠폰일 때
    public void throwIfCouponNotApplicableToProduct(Set<Long> companyIdSet, List<ProductEntity> productEntityList) {
        Set<Long> productCompanyIdSet = new HashSet<>();
        for (ProductEntity product : productEntityList) {
            productCompanyIdSet.add(product.getCompany().getCpId());
        }

        for (Long id : companyIdSet) {
            if (!productCompanyIdSet.contains(id)) {
                throw new ApiException(ErrorCode.BAD_REQUEST, "결제하시려는 상품에 적용할 수 없는 쿠폰이 있습니다.");
            }
        }
    }

    public Set<Long> companyIdSetThrowIfCompanyDuplicated(List<CouponEntity> couponEntityList) {
        Set<Long> companyIdSet = new HashSet<>();
        for (CouponEntity coupon : couponEntityList) {
            if (companyIdSet.add(coupon.getCompany().getCpId())) {
                throw new ApiException(ErrorCode.BAD_REQUEST, "중복된 회사 쿠폰이 있습니다.");
            }
        }

        return companyIdSet;
    }

    // 최소 금액을 만족했는지
    public void throwIfBelowCouponMinimumCost(List<CouponEntity> couponEntityList, List<ProductEntity> productEntityList) {
        Map<CompanyEntity, BigInteger> amountMap = new HashMap<>();
        for (ProductEntity product : productEntityList) {
            amountMap.put(product.getCompany(), amountMap.getOrDefault(product.getCompany(), BigInteger.ZERO).add(product.getPdPrice()));
        }

        for (CouponEntity coupon : couponEntityList) {
            BigInteger amount = amountMap.get(coupon.getCompany());
            if (amount.compareTo(coupon.getCouponMinimumCost()) < 0) {
                throw new ApiException(ErrorCode.BAD_REQUEST, "최소 사용 금액이 아닙니다.");
            }
        }
    }
}
