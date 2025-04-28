package aba3.lucid.domain.company.dto;


import aba3.lucid.domain.company.enums.SNS;
import lombok.*;


@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SnsRequest {
    private SNS name;
    private String url;

}
