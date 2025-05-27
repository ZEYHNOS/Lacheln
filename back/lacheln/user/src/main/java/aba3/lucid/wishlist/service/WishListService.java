package aba3.lucid.wishlist.service;

import aba3.lucid.domain.alert.dto.CompanyAlertDto;
import aba3.lucid.domain.user.entity.UsersEntity;
import aba3.lucid.domain.user.entity.WishListEntity;
import aba3.lucid.domain.user.repository.WishListRepository;
import aba3.lucid.user.service.UserService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class WishListService {

    private final WishListRepository wishListRepository;

    // 유저 ID를 통해 찜 목록 추출
    public List<WishListEntity> findByUserId(String userId)   {
        return wishListRepository.findAllByUsers_userId(userId);
    }

    // 찜 목록 추가
    public void addWishList(WishListEntity wishListEntity) {
        wishListRepository.save(wishListEntity);
    }
    
    // 찜 목록에서 제거
    @Transactional
    public void deleteWishList(UsersEntity user, Long productId) {
        wishListRepository.deleteByProductIdAndUsers(productId, user);
    }
}
