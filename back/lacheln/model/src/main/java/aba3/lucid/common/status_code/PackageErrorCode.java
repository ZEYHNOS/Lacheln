package aba3.lucid.common.status_code;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PackageErrorCode implements StatusCodeIfs {

    PRODUCT_ALREADY_REGISTERED(400, 30400, "이미 패키지에 상품이 등록되어있습니다."),
    PACKAGE_NOT_FOUND(404, 30404, "존재하지 않은 패키지입니다."),
    INVALID_PACKAGE_END_DATE(400, 30401, "패키지 종료일이 너무 빠릅니다(하루 이상 남아야합니다)"),
    INVALID_PACKAGE_REGISTRATION(400, 30402, "패키지 등록 조건에 맞지 않습니다."),
    UNAUTHORIZED_PACKAGE_ACCESS(400, 30405, "패키지 요청에 대한 접근 권한이 없습니다.")
    ;

    private final Integer httpStatusCode;
    private final Integer serverStatusCode;
    private final String description;

}