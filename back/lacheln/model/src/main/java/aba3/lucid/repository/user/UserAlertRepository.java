package aba3.lucid.repository.user;

import aba3.lucid.domain.user.UserAlertEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAlertRepository extends JpaRepository<UserAlertEntity, Long> {
}
