package aba3.lucid.domain.user.repository;

import aba3.lucid.domain.user.entity.UserExitEntity;
import aba3.lucid.domain.user.entity.UsersEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserExitRepository extends JpaRepository<UserExitEntity, String> {
}
