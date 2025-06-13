package aba3.lucid.subscribe;

import aba3.lucid.common.annotation.Business;
import aba3.lucid.common.api.API;
import aba3.lucid.domain.user.dto.SubscribeSearchResponse;
import aba3.lucid.domain.user.entity.SubscribeEntity;
import aba3.lucid.domain.user.entity.UsersEntity;
import aba3.lucid.user.service.UserService;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Business
@RequiredArgsConstructor
public class SubscribeBusiness {
    private final SubscribeService subscribeService;
    private final UserService userService;

    // 구독 목록 조회
    public API<List<SubscribeSearchResponse>> searchSubscribe(String userId)  {
        List<SubscribeEntity> subList = subscribeService.searchSubscribes(userId);
        List<SubscribeSearchResponse> responseList = new ArrayList<>();
        for (SubscribeEntity sub : subList) {
            SubscribeSearchResponse res = SubscribeSearchResponse.builder()
                    .subscribeId(sub.getSubscribeId())
                    .cpIds(sub.getCompanyId())
                    .build();
            responseList.add(res);
        }
        return API.OK(responseList);
    }

    // 구독 추가 로직
    public API<String> addSubscribe(String userId, List<Long> cpIds)    {
        UsersEntity user = userService.findByIdWithThrow(userId);
        for(Long cpId : cpIds) {
            SubscribeEntity subscribe = SubscribeEntity
                    .builder()
                    .companyId(cpId)
                    .user(user)
                    .build();
            subscribeService.addSubscribe(subscribe);
        }
        return API.OK("구독 추가 완료");
    }
    
    // 구독 취소 로직
    public API<String> deleteSubscribe(String userId, Long cpId) {
        UsersEntity user = userService.findByIdWithThrow(userId);
        subscribeService.deleteSubscribe(user, cpId);
        return API.OK("구독 삭제 완료");
    }
}
