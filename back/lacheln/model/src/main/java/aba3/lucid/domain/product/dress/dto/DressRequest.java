package aba3.lucid.domain.product.dress.dto;

import aba3.lucid.common.enums.BinaryChoice;
import aba3.lucid.common.enums.Color;
import aba3.lucid.domain.product.enums.DressSize;
import aba3.lucid.domain.product.dto.ProductRequest;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.Valid;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@ToString(callSuper = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class DressRequest extends ProductRequest {

    // 실내 여부
    @NotNull
    private BinaryChoice inAvailable;

    // 야외 여부
    @NotNull
    private BinaryChoice outAvailable;

    // 드레스 색상
    @NotNull
    private Color color;

    @NotNull
    private BinaryChoice overlap;

    @NotNull
    private BinaryChoice essential;

    // 해당 드레스 상품 사이즈 리스트
    @Valid
    private List<DressSizeDto> sizeList;


}
