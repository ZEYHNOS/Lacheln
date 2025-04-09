package aba3.lucid.wishlist.business;

import aba3.lucid.common.annotation.Business;
import aba3.lucid.common.api.API;
import aba3.lucid.domain.user.convertor.WishListConverter;
import aba3.lucid.domain.user.dto.UserWishListResponse;
import aba3.lucid.domain.user.dto.WishListDto;
import aba3.lucid.domain.user.entity.WishListEntity;
import aba3.lucid.user.service.UserService;
import aba3.lucid.wishlist.service.WishListService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

import java.io.IOException;
import java.util.List;

@Slf4j
@Business
@RequiredArgsConstructor
public class WishListBusiness {

    private final WishListService wishListService;
    private final UserService userService;
    private final WishListConverter wishListConverter;
    private final ObjectMapper objectMapper;

    public API<UserWishListResponse> findByUser(String userId) {
        List<WishListEntity> wishList = wishListService.findByUser(userId);
        UserWishListResponse response = UserWishListResponse.builder().build();
        return API.OK(response);
    }
}
