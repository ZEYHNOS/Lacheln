package aba3.lucid.domain.user.repository;

import aba3.lucid.domain.user.entity.UsersEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersRepository extends JpaRepository<UsersEntity, String> {
    UsersEntity findByUserName(String name);
    UsersEntity findByUserEmail(String email);
}
