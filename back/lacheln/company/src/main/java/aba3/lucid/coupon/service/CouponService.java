package aba3.lucid.coupon.service;

import aba3.lucid.common.exception.ApiException;
import aba3.lucid.common.status_code.ErrorCode;
import aba3.lucid.company.service.CompanyService;
import aba3.lucid.domain.company.entity.CompanyEntity;
import aba3.lucid.domain.coupon.entity.CouponEntity;
import aba3.lucid.domain.coupon.repository.CouponRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Slf4j
@Service
public class CouponService {

    private final SecureRandom RANDOM;

    private final CompanyService companyService;

    private final CouponRepository couponRepository;

    private final String CHARACTERS;
    private final int CODE_LENGTH;

    public CouponService(SecureRandom RANDOM,
                         CouponRepository couponRepository,
                         CompanyService companyService,
                         @Value("${coupon.characters}") String CHARACTERS,
                         @Value("${coupon.len}") int CODE_LENGTH
         ) {
        this.RANDOM = RANDOM;
        this.couponRepository = couponRepository;
        this.CHARACTERS = CHARACTERS;
        this.CODE_LENGTH = CODE_LENGTH;
        this.companyService = companyService;
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

    // 유저가 쿠폰 등록하기


    // 유저가 쿠폰 사용


    // 쿠폰 삭제
    public void deleteCouponById(CouponEntity coupon) {


    }


    // 쿠폰 수정
    public CouponEntity updateCoupon(CouponEntity coupon) {

        return couponRepository.save(coupon);
    }

    // 요청을 보낸 업체의 쿠폰인지 유무 확인
    public void validateCompanyCouponOwnership(CompanyEntity company, CouponEntity coupon) {
        if (!company.equals(coupon.getCompany())) {
            throw new ApiException(ErrorCode.UNAUTHORIZED);
        }
    }


    // Id를 통해 쿠폰 찾기
    public CouponEntity findByIdWithThrow(String couponId) {
        return couponRepository.findById(couponId)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND));
    }


    // 쿠폰 PK 생성
    public String createUniqueKey() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < CODE_LENGTH; i++) {
            sb.append(CHARACTERS.charAt(RANDOM.nextInt(CHARACTERS.length())));
        }
        return sb.toString();
    }

}
