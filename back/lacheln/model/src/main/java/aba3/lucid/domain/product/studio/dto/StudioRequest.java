package aba3.lucid.domain.product.studio.dto;

import aba3.lucid.common.enums.BinaryChoice;
import aba3.lucid.domain.product.dto.ProductRequest;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
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
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class StudioRequest extends ProductRequest {

    // 실내촬영여부
    @NotNull
    private BinaryChoice inAvailable;

    // 야외촬영여부
    @NotNull
    private BinaryChoice outAvailable;

    // 최대수용인원
    @Min(1)
    private int maxPeople;

    // 배경선택여부
    @NotNull
    private BinaryChoice bgOptions;

}
