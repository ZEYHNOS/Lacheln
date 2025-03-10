package aba3.lucid.dto.product;

import aba3.lucid.common.annotation.valid.BinaryChoiceValid;
import aba3.lucid.common.enums.BinaryChoice;
import aba3.lucid.domain.product.enums.ProductStatus;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigInteger;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class ProductRequest implements ProductRequestIfs {

    // 상품 이름
    @NotNull
    private String name;

    // 상품 가격
    @NotNull
    @PositiveOrZero
    private BigInteger price;

    // 상품 상태
    @NotNull
    @Size(max = 20)
    // TODO 공통 상태 regexp 만들기
    @Pattern(regexp = "ACTIVE|INACTIVE|PACKAGE|REMOVE", message = "상태값이 존재하지 않습니다.")
    private ProductStatus status;

    // 추천 상태
    @BinaryChoiceValid
    private BinaryChoice rec;

    // 소요 시간
    @NotNull
    @Min(30)
    private int taskTime;

    // 설명
    @NotNull
    private String description;

}
