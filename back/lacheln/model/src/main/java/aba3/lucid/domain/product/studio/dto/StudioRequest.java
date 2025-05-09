package aba3.lucid.domain.product.studio.dto;

import aba3.lucid.common.enums.BinaryChoice;
import aba3.lucid.domain.product.dto.ProductRequest;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@ToString(callSuper = true)
public class StudioRequest extends ProductRequest {

    // 실내촬영여부
    @NotNull
    private BinaryChoice stdInAvailable;

    // 야외촬영여부
    @NotNull
    private BinaryChoice stdOutAvailable;

    // 최대수용인원
    @Min(1)
    private int stdMaxPeople;

    // 배경선택여부
    @NotNull
    private BinaryChoice stdBgOptions;

}
