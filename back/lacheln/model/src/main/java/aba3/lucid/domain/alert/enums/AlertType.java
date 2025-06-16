package aba3.lucid.domain.alert.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AlertType {

//    PRODUCT,
//    PACKAGE,
//    SCHEDULE,
//    MESSAGE,
//    PAYMENT,
//    SUBSCRIBE,
//    BOARD,
//    REPORT

    INVITATION_PACKAGE(
            "PACKAGE",
            "%s님이 %s 패키지에 초대했습니다.",
            "/package/collaboration/%s"
            ),


    REVIEW(
            "REVIEW",
            "%s님이 리뷰를 작성하셨습니다.",
            "/company/review")



    ;

    private final String type;
    private final String text;
    private final String baseUrl;

}
