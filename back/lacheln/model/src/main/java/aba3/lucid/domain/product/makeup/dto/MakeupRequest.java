package aba3.lucid.domain.product.makeup.dto;

import aba3.lucid.common.enums.BinaryChoice;
import aba3.lucid.domain.product.dto.ProductRequest;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class MakeupRequest extends ProductRequest {

    // 출장 여부
    @NotNull
    private BinaryChoice businessTrip;

    // 방문 여부
    @NotNull
    private BinaryChoice visit;

    // 담당자
    @Size(min = 1, max = 5, message = "담당자 이름은 1~5")
    @NotBlank
    private String manager;


}
