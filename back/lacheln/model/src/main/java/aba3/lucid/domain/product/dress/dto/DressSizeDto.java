package aba3.lucid.domain.product.dress.dto;

import aba3.lucid.domain.product.enums.DressSize;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigInteger;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class DressSizeDto {

    @NotNull
    private DressSize size;

    @Min(1)
    private int stock;

    private BigInteger plusCost;

}
