package aba3.lucid.domain.user.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class SubscribeSearchResponse {
    private List<Long> cpIds;
}
