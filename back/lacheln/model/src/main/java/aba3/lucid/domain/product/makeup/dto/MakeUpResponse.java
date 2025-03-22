package aba3.lucid.domain.product.makeup.dto;

import aba3.lucid.common.enums.BinaryChoice;
import aba3.lucid.domain.product.dto.ProductDetailResponse;
import lombok.*;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class MakeUpResponse extends ProductDetailResponse {

    // 출장 여부
    private BinaryChoice businessTrip;

    // 방문 여부
    private BinaryChoice visit;

    // 담당자
    private String manager;

}
