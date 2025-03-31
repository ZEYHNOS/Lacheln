package aba3.lucid.email.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class EmailVerifyRequest {
    private String email;
    private String code;
}
