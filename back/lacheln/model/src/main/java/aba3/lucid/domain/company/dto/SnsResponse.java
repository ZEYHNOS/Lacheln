package aba3.lucid.domain.company.dto;


import aba3.lucid.domain.company.enums.SNS;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SnsResponse {
    private long snsId;
    private SNS snsName;
    private String snsUrl;


}
