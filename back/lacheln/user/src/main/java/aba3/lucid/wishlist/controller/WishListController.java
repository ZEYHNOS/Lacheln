package aba3.lucid.wishlist.controller;

import aba3.lucid.common.api.API;
import aba3.lucid.common.auth.AuthUtil;
import aba3.lucid.domain.user.dto.WishListAddRequest;
import aba3.lucid.domain.user.dto.WishListDeleteRequest;
import aba3.lucid.wishlist.business.WishListBusiness;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/user/wishlist")
public class WishListController {

    private final WishListBusiness wishListBusiness;

    // 세션에 존재하는 유저의 찜 목록 조회하여 찜한 상품들의 ID를 가져오는 Controller
    @GetMapping("/search")
    public API<List<Long>> findAll() {
        String id = AuthUtil.getUserId();
        List<Long> list = wishListBusiness.findByUser(id);
        return API.OK(list);
    }

    // 특정 상품을 찜 목록에 추가
    @PostMapping("/add")
    public API<String> add(@RequestBody WishListAddRequest wishListAddRequest) {
        return wishListBusiness.addWishList(AuthUtil.getUserId(), wishListAddRequest);
    }
    
    // 찜 목록에 있는 상품 제거
    @DeleteMapping("/delete")
    public API<String> delete(@RequestBody WishListDeleteRequest wishListDeleteRequest) {
        return wishListBusiness.deleteWishList(AuthUtil.getUserId(), wishListDeleteRequest);
    }
}
