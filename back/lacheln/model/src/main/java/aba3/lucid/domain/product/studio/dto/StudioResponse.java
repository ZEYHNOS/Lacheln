package aba3.lucid.domain.product.studio.dto;

import aba3.lucid.common.enums.BinaryChoice;
import aba3.lucid.domain.product.dto.ProductDetailResponse;
import lombok.*;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@ToString(callSuper = true)
public class StudioResponse extends ProductDetailResponse {

    // 실내촬영여부
    private BinaryChoice inAvailable;

    // 야외촬영여부
    private BinaryChoice outAvailable;

    // 최대수용인원
    private int maxPeople;

    // 배경선택여부
    private BinaryChoice bgOptions;
}
