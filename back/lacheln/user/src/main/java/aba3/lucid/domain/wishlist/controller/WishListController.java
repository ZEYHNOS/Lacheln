package aba3.lucid.domain.wishlist.controller;

import aba3.lucid.common.api.API;
import aba3.lucid.common.auth.AuthUtil;
import aba3.lucid.domain.user.entity.WishListEntity;
import aba3.lucid.domain.user.repository.WishListRepository;
import aba3.lucid.domain.wishlist.business.WishListBusiness;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user/wishlist")
public class WishListController {

    private final WishListBusiness wishListBusiness;

    @GetMapping("/")
    public API<List<WishListEntity>> findAll() {
        String id = AuthUtil.getUserId();
        return wishListBusiness.findByUser(id);
    }
}
