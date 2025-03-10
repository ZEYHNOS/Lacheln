package aba3.lucid.dto.product.option;

import aba3.lucid.common.annotation.valid.BinaryChoiceValid;
import aba3.lucid.common.enums.ActiveEnum;
import aba3.lucid.common.enums.BinaryChoice;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Negative;
import jakarta.validation.constraints.NegativeOrZero;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.math.BigInteger;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class OptionRequest {

    @NotBlank
    private String name;

    // 중복 여부
    @BinaryChoiceValid
    private BinaryChoice overlap;

    // 필수 여부
    @BinaryChoiceValid
    private BinaryChoice essential;

    // 활성화 여부
    // todo 상태 관련 처리하기
    private ActiveEnum status;

    // 옵션 상세 정보
    @Valid
    private List<OptionDetailRequest> optionDtList;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @ToString
    private static class OptionDetailRequest {

        // 옵션 상세 이름
        @NotBlank
        private String opDtName;

        // 추가금
        @NegativeOrZero
        private BigInteger plusCost;

        // 추가 시간
        @Negative
        private int plusTime;
    }

}
