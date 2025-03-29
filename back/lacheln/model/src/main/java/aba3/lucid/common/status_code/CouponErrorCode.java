package aba3.lucid.common.status_code;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CouponErrorCode implements StatusCodeIfs {
    // 쿠폰 20000번대

    COUPON_NOT_FOUND(404, 20404, "쿠폰을 찾을 수 없습니다."),
    EXPIRED_COUPON(400, 20400, "쿠폰의 유효기간이 지났습니다."),
    NO_COUPON_OWNERSHIP(400, 20401, "쿠폰을 소유하고 있지 않습니다."),
    USED_COUPON(400, 20402, "사용한 쿠폰입니다."),
    UNAUTHORIZED_PACKAGE_ACCESS(400, 20403, "쿠폰에 접근 권한이 없습니다."),



    ;


    private final Integer httpStatusCode;
    private final Integer serverStatusCode;
    private final String description;
}