package aba3.lucid.sms.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SmsSendResponse {
    private String phoneNum;
}
