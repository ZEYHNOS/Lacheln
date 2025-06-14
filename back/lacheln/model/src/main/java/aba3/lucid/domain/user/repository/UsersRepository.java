package aba3.lucid.domain.user.repository;

import aba3.lucid.domain.user.entity.UsersEntity;
import org.apache.catalina.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<UsersEntity, String> {
    @Query("SELECT COUNT(u) FROM UsersEntity u WHERE DATE(u.userJoinDate) = :date")
    long countByUserJoinDate(@Param("date") LocalDate date);

    // 월별 가입자 수 조회 (월, 카운트 순으로 반환)
    @Query(value = "SELECT MONTH(user_join_date) as month, COUNT(*) as count " +
            "FROM users " +
            "WHERE YEAR(user_join_date) = YEAR(NOW()) " +
            "GROUP BY MONTH(user_join_date) " +
            "ORDER BY month",
            nativeQuery = true)
    List<Object[]> countMonthlyJoin();

    UsersEntity findByUserName(String name);
    Optional<UsersEntity> findByUserEmail(String email);
    boolean existsByUserPhone(String phone);
}