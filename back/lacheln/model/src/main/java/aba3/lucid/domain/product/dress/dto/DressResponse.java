package aba3.lucid.domain.product.dress.dto;

import aba3.lucid.common.enums.BinaryChoice;
import aba3.lucid.common.enums.Color;
import aba3.lucid.domain.product.dto.ProductDetailResponse;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@ToString
public class DressResponse extends ProductDetailResponse {

    // 실내 여부
    private BinaryChoice inAvailable;

    // 야외 여부
    private BinaryChoice outAvailable;

    // 드레스 색상
    private Color color;

    // 드레스 사이즈
    private List<DressSizeDto> dressSizeList;

}