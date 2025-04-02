package aba3.lucid.domain.wishlist.business;

import aba3.lucid.common.annotation.Business;
import aba3.lucid.common.api.API;
import aba3.lucid.domain.company.entity.CompanyEntity;
import aba3.lucid.domain.product.entity.ProductEntity;
import aba3.lucid.domain.user.convertor.WishListConverter;
import aba3.lucid.domain.user.dto.UserWishListResponse;
import aba3.lucid.domain.user.dto.WishListDto;
import aba3.lucid.domain.user.entity.UsersEntity;
import aba3.lucid.domain.user.entity.WishListEntity;
import aba3.lucid.domain.user.service.UserService;
import aba3.lucid.domain.wishlist.service.WishListService;
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
