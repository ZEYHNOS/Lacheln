package aba3.lucid.domain.wishlist.business;

import aba3.lucid.common.annotation.Business;
import aba3.lucid.common.api.API;
import aba3.lucid.domain.user.entity.UsersEntity;
import aba3.lucid.domain.user.entity.WishListEntity;
import aba3.lucid.domain.wishlist.service.WishListService;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Business
@RequiredArgsConstructor
public class WishListBusiness {

    private final WishListService wishListService;

    public API<List<WishListEntity>> findByUser(String userId) {
        List<WishListEntity> wishlist = wishListService.findByUser(userId);
        return API.OK(wishlist);
    }

}
