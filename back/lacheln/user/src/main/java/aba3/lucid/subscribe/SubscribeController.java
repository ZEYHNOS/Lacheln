package aba3.lucid.subscribe;

import aba3.lucid.common.api.API;
import aba3.lucid.common.auth.AuthUtil;
import aba3.lucid.domain.user.dto.SubscribeAddRequest;
import aba3.lucid.domain.user.dto.SubscribeDeleteRequest;
import aba3.lucid.domain.user.dto.SubscribeSearchResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/subscribe")
@RequiredArgsConstructor
public class SubscribeController {
    private final SubscribeBusiness subscribeBusiness;

    // 현재 세션 사용자 구독 목록 조회
    @GetMapping("/search")
    public API<SubscribeSearchResponse> searchSubscribe()   {
        return subscribeBusiness.searchSubscribe(AuthUtil.getUserId());
    }

    // 현재 세션 사용자 구독 목록 추가
    @PostMapping("/add")
    public API<String> addSubscribe(@RequestBody SubscribeAddRequest addRequest) {
        return subscribeBusiness.addSubscribe(AuthUtil.getUserId(), addRequest.getCpIds());
    }

    // 현재 세션 사용자 구독 목록 제거
    @DeleteMapping("/delete")
    public API<String> deleteSubscribe(@RequestBody SubscribeDeleteRequest deleteRequest) {
        return subscribeBusiness.deleteSubscribe(AuthUtil.getUserId(), deleteRequest.getCpIds());
    }
}
