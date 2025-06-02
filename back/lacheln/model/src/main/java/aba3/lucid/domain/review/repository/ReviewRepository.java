package aba3.lucid.domain.review.repository;

import aba3.lucid.domain.review.entity.ReviewEntity;
import aba3.lucid.domain.user.entity.UsersEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<ReviewEntity, Long> {

    List<ReviewEntity> findAllByProductId(Long productId);

    List<ReviewEntity> findAllByCompanyId(Long companyId);

    List<ReviewEntity> findAllByPayDetailEntity_PayManagement_User(UsersEntity user);
}
