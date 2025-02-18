package aba3.lucid.social.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SNS {

    INSTAGRAM("static/images/sns/instagram.png"),
    NAVER_CAFE("static/images/sns/naver_cafe.png"),
    YOUTUBE("static/images/sns/youtube.png")
    ;

    private final String imageUrl;

}
