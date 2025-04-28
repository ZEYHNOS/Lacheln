package aba3.lucid.wishlist.controller;

import aba3.lucid.common.api.API;
import aba3.lucid.common.auth.AuthUtil;
import aba3.lucid.domain.user.dto.WishListAddRequest;
import aba3.lucid.domain.user.dto.WishListDeleteRequest;
import aba3.lucid.wishlist.business.WishListBusiness;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/user/wishlist")
@Tag(name = "WishList Controller", description = "찜 관련 API")
public class WishListController {

    private final WishListBusiness wishListBusiness;

    // 세션에 존재하는 유저의 찜 목록 조회하여 찜한 상품들의 ID를 가져오는 Controller
    @GetMapping("/search")
    @Operation(summary = "상품 찜 목록 조회", description = "해당하는 소비자의 찜 목록을 조회합니다.")
    public API<List<Long>> findAll() {
        String id = AuthUtil.getUserId();
        List<Long> list = wishListBusiness.findByUser(id);
        return API.OK(list);
    }

    // 특정 상품을 찜 목록에 추가
    @PostMapping("/add")
    @Operation(summary = "상품 찜 목록 추가", description = "해당하는 소비자의 찜 목록을 추가합니다.")
    public API<String> add(@RequestBody WishListAddRequest wishListAddRequest) {
        return wishListBusiness.addWishList(AuthUtil.getUserId(), wishListAddRequest);
    }
    
    // 찜 목록에 있는 상품 제거
    @DeleteMapping("/delete")
    @Operation(summary = "상품 찜 목록 제거", description = "해당하는 소비자의 찜 목록을 제거합니다.")
    public API<String> delete(@RequestBody WishListDeleteRequest wishListDeleteRequest) {
        return wishListBusiness.deleteWishList(AuthUtil.getUserId(), wishListDeleteRequest);
    }
}
