package aba3.lucid.user.db;

import aba3.lucid.user.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<String, UserEntity> {

}
