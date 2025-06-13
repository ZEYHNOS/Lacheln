package aba3.lucid.subscribe;

import aba3.lucid.common.api.API;
import aba3.lucid.common.auth.AuthUtil;
import aba3.lucid.common.auth.CustomUserDetails;
import aba3.lucid.domain.user.dto.SubscribeAddRequest;
import aba3.lucid.domain.user.dto.SubscribeDeleteRequest;
import aba3.lucid.domain.user.dto.SubscribeSearchResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/subscribe")
@Tag(name = "Subscribe Controller", description = "구독 관련 API")
public class SubscribeController {
    private final SubscribeBusiness subscribeBusiness;

    // 현재 세션 사용자 구독 목록 조회
    @GetMapping("/search")
    @Operation(summary = "사용자 구독 목록 조회", description = "해당하는 소비자의 구독 목록을 조회합니다.")
    public API<List<SubscribeSearchResponse>> searchSubscribe(
            @AuthenticationPrincipal CustomUserDetails user
    )   {
        return subscribeBusiness.searchSubscribe(user.getUserId());
    }

    // 현재 세션 사용자 구독 목록 추가
    @PostMapping("/add")
    @Operation(summary = "사용자 구독 추가", description = "해당하는 소비자의 구독 목록을 추가합니다.")
    public API<String> addSubscribe(
            @RequestBody SubscribeAddRequest addRequest,
            @AuthenticationPrincipal CustomUserDetails user) {
        return subscribeBusiness.addSubscribe(user.getUserId(), addRequest.getCpIds());
    }

    // 현재 세션 사용자 구독 목록 제거
    @DeleteMapping("/delete/{companyId}")
    @Operation(summary = "사용자 구독 취소", description = "해당하는 소비자의 구독 목록을 제거합니다.")
    public API<String> deleteSubscribe(
            @PathVariable Long companyId,
            @AuthenticationPrincipal CustomUserDetails user) {
        return subscribeBusiness.deleteSubscribe(user.getUserId(), companyId);
    }
}
