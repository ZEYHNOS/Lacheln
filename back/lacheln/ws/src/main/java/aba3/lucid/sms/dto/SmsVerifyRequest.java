package aba3.lucid.sms.dto;

import lombok.Getter;

@Getter
public class SmsVerifyRequest {
    private String phoneNum;
    private String code;
}
