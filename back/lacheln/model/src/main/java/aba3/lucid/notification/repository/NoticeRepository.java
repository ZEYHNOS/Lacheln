package aba3.lucid.notification.repository;

import aba3.lucid.notification.model.UserNoticeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeRepository extends JpaRepository<String, UserNoticeEntity> {

}
