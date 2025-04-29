package aba3.lucid.coupon.service;

import aba3.lucid.common.exception.ApiException;
import aba3.lucid.common.status_code.CouponErrorCode;
import aba3.lucid.domain.company.entity.CompanyEntity;
import aba3.lucid.domain.coupon.entity.CouponBoxEntity;
import aba3.lucid.domain.coupon.entity.CouponEntity;
import aba3.lucid.domain.coupon.enums.CouponBoxStatus;
import aba3.lucid.domain.coupon.repository.CouponRepository;
import aba3.lucid.domain.product.entity.ProductEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
public class CouponService {

    private final CouponBoxService couponBoxService;

    private final SecureRandom RANDOM;

    private final CouponRepository couponRepository;

    private final String CHARACTERS;
    private final int CODE_LENGTH;

    // TODO 리팩토링하기
    public CouponService(SecureRandom RANDOM,
                         CouponRepository couponRepository,
                         @Value("${coupon.characters}") String CHARACTERS,
                         @Value("${coupon.len}") int CODE_LENGTH,
                         CouponBoxService couponBoxService
         ) {
        this.RANDOM = RANDOM;
        this.couponRepository = couponRepository;
        this.CHARACTERS = CHARACTERS;
        this.CODE_LENGTH = CODE_LENGTH;
        this.couponBoxService = couponBoxService;
    }


    // 업체에서 쿠폰 등록(업체, 상품)
    public CouponEntity issueCouponForCompany(CouponEntity entity) {
        String id;

        do {
            id = createUniqueKey();
        } while (couponRepository.existsById(id));

        entity.setCouponId(id);
        return couponRepository.save(entity);
    }


    // 쿠폰 삭제
    public void deleteCouponById(CouponEntity coupon) {
        couponRepository.delete(coupon);
    }


    // 쿠폰 수정
    public CouponEntity updateCoupon(CouponEntity coupon) {
        return couponRepository.save(coupon);
    }

    // 요청을 보낸 업체의 쿠폰인지 유무 확인
    public void validateCompanyCouponOwnership(CompanyEntity company, CouponEntity coupon) {
        if (!company.equals(coupon.getCompany())) {
            throw new ApiException(CouponErrorCode.UNAUTHORIZED_PACKAGE_ACCESS);
        }
    }



    // 유효기간을 지난 쿠폰인지
    public boolean isNotCouponExpired(CouponEntity coupon) {
        return coupon.getCouponExpirationDate().isBefore(LocalDateTime.now());
    }


    // Id를 통해 쿠폰 찾기
    public CouponEntity findByIdWithThrow(String couponId) {
        return couponRepository.findById(couponId)
                .orElseThrow(() -> new ApiException(CouponErrorCode.COUPON_NOT_FOUND));
    }


    // 쿠폰 PK 생성
    public String createUniqueKey() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < CODE_LENGTH; i++) {
            sb.append(CHARACTERS.charAt(RANDOM.nextInt(CHARACTERS.length())));
        }
        return sb.toString();
    }

    // TODO 리팩토링
    public void verificationBeforePayment(String userId, List<CouponBoxEntity> couponBoxEntityList, List<ProductEntity> productEntityList) {
        // 쿠폰을 소유하고 있는지
        if (couponBoxService.isUserDoesNotOwnCoupon(userId, couponBoxEntityList)) {
            // TODO 소유하고 있지 않을 때

        }

        // 쿠폰 상태 확인
        for (CouponBoxEntity couponBox : couponBoxEntityList) {
            if (!couponBox.getCouponStatus().equals(CouponBoxStatus.UNUSED)) {
                // 사용했거나 만료되었을 때 처리하기
            }
        }

        // 쿠폰 유효기간 확인하기
        List<CouponEntity> couponEntityList =couponBoxEntityList.stream()
                .map(CouponBoxEntity::getCoupon)
                .toList();

        for (CouponEntity coupon : couponEntityList) {
            if (isNotCouponExpired(coupon)) {
                // TODO 유효기간이 지났을 때 처리하기
            }
        }

        // 같은 업체에서 발행한 쿠폰을 2개 이상 사용하는가
        Set<Long> companyIdSet = new HashSet<>();
        for (CouponEntity coupon : couponEntityList) {
            if (companyIdSet.add(coupon.getCompany().getCpId())) {
                // 같은 업체의 쿠폰을 2개 이상 사용했을 때 처리하기
            }
        }

        Set<Long> productCompanyIdSet = new HashSet<>();
        for (ProductEntity product : productEntityList) {
            productCompanyIdSet.add(product.getCompany().getCpId());
        }

        // 사용하는 쿠폰이 결제하려는 상품과 무관한 쿠폰일 때
        for (Long id : companyIdSet) {
            if (!productCompanyIdSet.contains(id)) {

            }
        }


        // 쿠폰 최소 사용 금액인가
        Map<CompanyEntity, BigInteger> amountMap = new HashMap<>();
        for (ProductEntity product : productEntityList) {
            amountMap.put(product.getCompany(), amountMap.getOrDefault(product.getCompany(), BigInteger.ZERO).add(product.getPdPrice()));
        }

        for (CouponEntity coupon : couponEntityList) {
            BigInteger amount = amountMap.get(coupon.getCompany());
            if (amount.compareTo(coupon.getCouponMinimumCost()) < 0) {

            }
        }
    }
}
