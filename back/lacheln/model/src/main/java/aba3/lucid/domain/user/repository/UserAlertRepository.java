package aba3.lucid.domain.user.repository;

import aba3.lucid.domain.user.entity.UserAlertEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAlertRepository extends JpaRepository<UserAlertEntity, Long> {

}
