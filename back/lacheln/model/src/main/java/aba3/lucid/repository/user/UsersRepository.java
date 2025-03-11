package aba3.lucid.repository.user;

import aba3.lucid.domain.user.UsersEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersRepository extends JpaRepository<UsersEntity, String> {
    UsersEntity findByUsername(String name);
}
