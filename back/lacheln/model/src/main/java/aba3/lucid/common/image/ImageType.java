package aba3.lucid.common.image;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ImageType {

    PROFILE("profile"),
    PRODUCT("product"),
    REPORT("report"),
    REVIEW("review")
    ;

    private final String type;
}
