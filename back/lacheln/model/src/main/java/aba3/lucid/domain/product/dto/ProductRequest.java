package aba3.lucid.domain.product.dto;

import aba3.lucid.common.annotation.ValidList;
import aba3.lucid.common.enums.BinaryChoice;
import aba3.lucid.domain.company.entity.CompanyCountryEntity;
import aba3.lucid.domain.product.dto.option.OptionDto;
import aba3.lucid.domain.product.enums.ProductStatus;
import aba3.lucid.domain.user.enums.CountryEnum;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.math.BigInteger;
import java.time.LocalTime;
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
    private BigInteger price;

    // 추천 상태
    @NotNull
    private BinaryChoice rec;

    // 소요 시간
    @NotNull
    private LocalTime taskTime;

    // 설명
    @Valid
    @ValidList
    private List<DescriptionRequest> descriptionList;

    @ValidList
    private List<String> imageUrlList;

    private List<String> hashTagList;

    @Valid
    private List<OptionDto> optionList;

}
