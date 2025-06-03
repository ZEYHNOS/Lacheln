package aba3.lucid.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AlertType {

    INVITATION_PACKAGE("패키지 초대"
            , "%s님이 %s 패키지에 초대를 했습니다.\n만약 원치 않은 초대를 받았을 경우 나가기 및 신고 버튼을 눌러주세요."
            , "/package/info/%d"),

    SYSTEM_ALERT("시스템 알림"
            , "시스템에서 중요한 공지를 전달합니다: %s"
            , null),

    REVIEW_COMMENT("리뷰 답글",
            "%s님에게 답글이 달렸습니다.",
            "/comment/search/{reviewCommentId}")

    ;

    private final String title;
    private final String content;
    private final String accessUrl;
}