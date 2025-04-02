package aba3.lucid.domain.wishlist.service;

import aba3.lucid.domain.user.entity.UsersEntity;
import aba3.lucid.domain.user.entity.WishListEntity;
import aba3.lucid.domain.user.repository.UsersRepository;
import aba3.lucid.domain.user.repository.WishListRepository;
import aba3.lucid.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class WishListService {

    private final WishListRepository wishListRepository;
    private final UserService userService;

    public List<WishListEntity> findByUser(String userId)   {
        UsersEntity user = userService.findById(userId);
        return wishListRepository.findByUsers(user);
    }
}
