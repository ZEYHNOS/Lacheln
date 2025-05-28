package aba3.lucid.wishlist.business;

import aba3.lucid.common.annotation.Business;
import aba3.lucid.common.api.API;
import aba3.lucid.domain.user.dto.WishListAddRequest;
import aba3.lucid.domain.user.dto.WishListDeleteRequest;
import aba3.lucid.domain.user.entity.UsersEntity;
import aba3.lucid.domain.user.entity.WishListEntity;
import aba3.lucid.user.service.UserService;
import aba3.lucid.wishlist.service.WishListService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@Business
@RequiredArgsConstructor
public class WishListBusiness {

    private final WishListService wishListService;
    private final UserService userService;

    // 유저 ID로 가져온 찜목록을 상품 ID만 List에 ADD
    public List<Long> findByUser(String userId) {
        return wishListService.findByUserId(userId).stream()
                .map(WishListEntity::getPdId)
                .toList()
                ;
    }
    
    // 찜 목록에 상품 추가
    public API<String> addWishList(String userId, WishListAddRequest request) {
        UsersEntity user = userService.findByIdWithThrow(userId);
        for(Long id : request.getIds()) {
            WishListEntity wishList = WishListEntity
                    .builder()
                    .users(user)
                    .pdId(id)
                    .build();
            wishListService.addWishList(wishList);
        }
        return API.OK("위시 리스트 추가완료..");
    }
    
    // 찜 목록에 있는 상품 제거
    public API<String> deleteWishList(String userId, WishListDeleteRequest request) {
        UsersEntity user = userService.findByIdWithThrow(userId);
        for(Long wishId : request.getIds()) {
            wishListService.deleteWishList(user, wishId);
        }
        return API.OK("위시 리스트 상품 제거 완료");
    }
}
