package aba3.lucid.domain.product.dto;

import aba3.lucid.common.enums.BinaryChoice;
import aba3.lucid.domain.product.entity.ProductImageEntity;
import aba3.lucid.domain.product.enums.ProductStatus;
import aba3.lucid.domain.product.dto.option.OptionDto;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.math.BigInteger;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@ToString
public class ProductRequest implements ProductRequestIfs {

    // 상품 이름
    @NotBlank
    @Size(max = 50, min = 2)
    private String name;

    // 상품 가격
    @NotNull
    @PositiveOrZero
    private BigInteger price;

    // 상품 상태
    @NotNull
    private ProductStatus status;

    // 추천 상태
    @NotNull
    private BinaryChoice rec;

    // 소요 시간
    @Min(value = 30, message = "최소 작업시간 30분입니다.")
    private int taskTime;

    // 설명
    @NotBlank
    private String description;

    private List<String> imageUrlList;

    private List<String> hashTagList;

    private List<OptionDto> optionList;


    @AssertTrue(message = "이미지가 없습니다.")
    public boolean imageUrlInvalidator() {
        return !imageUrlList.isEmpty();
    }

}
