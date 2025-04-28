package aba3.lucid.domain.user.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class SubscribeAddRequest {
    private List<Long> cpIds;
}
