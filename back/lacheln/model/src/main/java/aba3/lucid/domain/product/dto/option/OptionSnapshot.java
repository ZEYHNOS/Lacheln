package aba3.lucid.domain.product.dto.option;

import lombok.*;

import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OptionSnapshot {

    private Long optionId; //옵션ID

    private String optionName; // 옵션 이름

    private Long optionDetailId; //옵션상세ID

    private String optionDetailName; // 옵션 상세 이름

    private LocalTime optionTaskTime; // 옵션 작업 시간

}