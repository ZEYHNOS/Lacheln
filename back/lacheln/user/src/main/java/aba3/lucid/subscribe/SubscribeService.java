package aba3.lucid.subscribe;

import aba3.lucid.domain.user.entity.SubscribeEntity;
import aba3.lucid.domain.user.entity.UsersEntity;
import aba3.lucid.domain.user.repository.SubscribeRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SubscribeService {
    private final SubscribeRepository subscribeRepository;

    // 사용자 ID기반 구독목록 조회
    public List<SubscribeEntity> searchSubscribes(String userId)    {
        return subscribeRepository.findAllByUser_UserId(userId);
    }

    // 구독 추가
    public void addSubscribe(SubscribeEntity subscribe)    {
        subscribeRepository.save(subscribe);
    }

    // 구독 취소
    @Transactional
    public void deleteSubscribe(UsersEntity user, Long cpId) {
        subscribeRepository.deleteByUserAndCompanyId(user, cpId);
    }
}
