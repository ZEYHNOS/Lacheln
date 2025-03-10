package aba3.lucid.dto.product.dress;

import aba3.lucid.common.annotation.valid.BinaryChoiceValid;
import aba3.lucid.common.enums.BinaryChoice;
import aba3.lucid.common.enums.Color;
import aba3.lucid.dto.product.ProductRequest;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class DressRequest extends ProductRequest {

    // 실내 여부
    @BinaryChoiceValid
    private BinaryChoice inAvailable;

    // 야외 여부
    @BinaryChoiceValid
    private BinaryChoice outAvailable;

    // 드레스 색상
    @NotNull
    private Color color;

    // 드레스 수량
    @Min(1)
    private int stock;

    // 드레스 사이즈
    @NotBlank
    private String size;

}
