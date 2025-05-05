package aba3.lucid.domain.product.dto.option;

import aba3.lucid.common.enums.ActiveEnum;
import aba3.lucid.common.enums.BinaryChoice;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Negative;
import jakarta.validation.constraints.NegativeOrZero;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigInteger;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class OptionDto {

    @NotBlank
    private String name;

    // 중복 여부
    @NotNull
    private BinaryChoice overlap;

    // 필수 여부
    @NotNull
    private BinaryChoice essential;

    // 활성화 여부
    @NotNull
    private ActiveEnum status;

    // 옵션 상세 정보
    @Valid
    private List<OptionDetailDto> optionDtList;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @ToString
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class OptionDetailDto {

        // 옵션 상세 이름
        @NotBlank
        private String opDtName;

        // 추가금
        @NotNull
        @NegativeOrZero
        private BigInteger plusCost;

        // 추가 시간
        @Negative
        private int plusTime;

    }




}
