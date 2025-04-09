package aba3.lucid.wishlist.business;

import aba3.lucid.common.annotation.Business;
import aba3.lucid.common.api.API;
import aba3.lucid.domain.user.convertor.WishListConverter;
import aba3.lucid.domain.user.dto.UserWishListResponse;
import aba3.lucid.domain.user.entity.WishListEntity;
import aba3.lucid.user.service.UserService;
import aba3.lucid.wishlist.service.WishListService;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Business
@RequiredArgsConstructor
public class WishListBusiness {

    private final WishListService wishListService;
    private final UserService userService;
    private final WishListConverter wishListConverter;

    public API<UserWishListResponse> findByUser(String userId) {
        List<WishListEntity> wishList = wishListService.findByUser(userId);
        UserWishListResponse response = UserWishListResponse.builder().build();
        return API.OK(response);
    }

}
