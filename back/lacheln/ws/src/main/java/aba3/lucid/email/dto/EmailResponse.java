package aba3.lucid.email.dto;

import aba3.lucid.email.enums.EmailCodes;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class EmailResponse {
    private EmailCodes message;
}
