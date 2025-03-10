package aba3.lucid.dto.product.dress;

import aba3.lucid.common.enums.BinaryChoice;
import aba3.lucid.common.enums.Color;
import aba3.lucid.dto.product.ProductResponse;
import lombok.*;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@ToString
public class DressResponse extends ProductResponse {

    // 실내 여부
    private BinaryChoice inAvailable;

    // 야외 여부
    private BinaryChoice outAvailable;

    // 드레스 색상
    private Color color;

    // 드레스 수량
    private int stock;

    // 드레스 사이즈
    private String size;

}