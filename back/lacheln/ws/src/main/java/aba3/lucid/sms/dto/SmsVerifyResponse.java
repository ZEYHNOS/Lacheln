package aba3.lucid.sms.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SmsVerifyResponse {
    private String phoneNum;
    private Boolean verified;
}
