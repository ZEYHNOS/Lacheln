package aba3.lucid.domain.user.repository;

import aba3.lucid.domain.user.entity.UsersEntity;
import org.apache.catalina.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<UsersEntity, String> {
    UsersEntity findByUserName(String name);
    Optional<UsersEntity> findByUserEmail(String email);
    boolean existsByUserPhone(String phone);
}