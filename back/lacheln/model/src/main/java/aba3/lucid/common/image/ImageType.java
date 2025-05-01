package aba3.lucid.common.image;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ImageType {

    PROFILE("profile"),
    PRODUCT_IMAGE("product")
    ;

    private final String type;
}
