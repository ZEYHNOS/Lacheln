package aba3.lucid.wishlist.controller;

import aba3.lucid.common.api.API;
import aba3.lucid.common.auth.AuthUtil;
import aba3.lucid.domain.user.dto.UserWishListResponse;
import aba3.lucid.wishlist.business.WishListBusiness;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/user/wishlist")
public class WishListController {

    private final WishListBusiness wishListBusiness;

    @GetMapping("/")
    public API<List<Long>> findAll() {
        String id = AuthUtil.getUserId();
        List<Long> list = wishListBusiness.findByUser(id);
        log.info("{}", list);
        return API.OK(list);
    }
}
