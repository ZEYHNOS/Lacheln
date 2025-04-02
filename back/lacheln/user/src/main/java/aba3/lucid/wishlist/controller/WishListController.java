package aba3.lucid.wishlist.controller;

import aba3.lucid.common.api.API;
import aba3.lucid.common.auth.AuthUtil;
import aba3.lucid.domain.user.dto.UserWishListResponse;
import aba3.lucid.wishlist.business.WishListBusiness;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user/wishlist")
public class WishListController {

    private final WishListBusiness wishListBusiness;

    @GetMapping("/")
    public API<UserWishListResponse> findAll() {
        String id = AuthUtil.getUserId();
        return wishListBusiness.findByUser(id);
    }
}
