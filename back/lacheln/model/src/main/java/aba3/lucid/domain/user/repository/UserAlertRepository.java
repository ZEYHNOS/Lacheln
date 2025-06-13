package aba3.lucid.domain.user.repository;

import aba3.lucid.domain.user.entity.UserAlertEntity;
import aba3.lucid.domain.user.entity.UsersEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserAlertRepository extends JpaRepository<UserAlertEntity, Long> {

    List<UserAlertEntity> findAllByUsers(UsersEntity user);
}
