package aba3.lucid.domain.user.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserPasswordVerifyRequest {
    private String password;
}
