package aba3.lucid.user.repository;

import aba3.lucid.user.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<String, UserEntity> {

}
