package aba3.lucid.domain.product.dto;

import aba3.lucid.common.enums.BinaryChoice;
import aba3.lucid.domain.company.entity.CompanyCountryEntity;
import aba3.lucid.domain.product.dto.option.OptionDto;
import aba3.lucid.domain.product.enums.ProductStatus;
import aba3.lucid.domain.user.enums.CountryEnum;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
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
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
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
    private List<DescriptionRequest> descriptionList;

    private List<String> imageUrlList;

    private List<String> hashTagList;

    private List<OptionDto> optionList;

}
