package aba3.lucid.email.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class EmailSendRequest {
    private String email;
}
