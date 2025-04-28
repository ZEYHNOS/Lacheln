package aba3.lucid.domain.user.repository;

import aba3.lucid.domain.user.entity.SubscribeEntity;
import aba3.lucid.domain.user.entity.UsersEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubscribeRepository extends JpaRepository<SubscribeEntity, Long> {
    List<SubscribeEntity> findAllByUser_UserId(String userId);
    void deleteByUserAndCompanyId(UsersEntity user, Long companyId);
}
