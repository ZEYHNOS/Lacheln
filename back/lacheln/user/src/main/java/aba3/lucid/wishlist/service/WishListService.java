package aba3.lucid.wishlist.service;

import aba3.lucid.domain.alert.dto.CompanyAlertDto;
import aba3.lucid.domain.user.dto.WishListDto;
import aba3.lucid.domain.user.entity.UsersEntity;
import aba3.lucid.domain.user.entity.WishListEntity;
import aba3.lucid.domain.user.repository.WishListRepository;
import aba3.lucid.user.service.UserService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
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
    private final UserService userService;
    private final ObjectMapper objectMapper;

    public List<WishListEntity> findByUserId(String userId)   {
        return wishListRepository.findAllByUsers_userId(userId);
    }
}
