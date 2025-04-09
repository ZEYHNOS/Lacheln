package aba3.lucid.domain.user.repository;

import aba3.lucid.domain.user.entity.UsersEntity;
import aba3.lucid.domain.user.entity.WishListEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WishListRepository extends JpaRepository<WishListEntity, Long> {
    List<WishListEntity> findByUsers(UsersEntity users);
}
