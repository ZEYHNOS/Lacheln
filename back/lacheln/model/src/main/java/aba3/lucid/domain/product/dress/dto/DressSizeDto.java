package aba3.lucid.domain.product.dress.dto;

import aba3.lucid.domain.product.enums.DressSize;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class DressSizeDto {

    @NotNull
    private DressSize size;

    @Min(1)
    private int stock;

    private int plusCost;

}
