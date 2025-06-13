package aba3.lucid.alert;

import aba3.lucid.common.exception.ApiException;
import aba3.lucid.common.status_code.ErrorCode;
import aba3.lucid.domain.user.entity.UserAlertEntity;
import aba3.lucid.domain.user.entity.UsersEntity;
import aba3.lucid.domain.user.repository.UserAlertRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserAlertService {

    private final UserAlertRepository userAlertRepository;

    public void alertRegister(UserAlertEntity entity) {
        userAlertRepository.save(entity);
    }

    public List<UserAlertEntity> getUserAlertListByUser(UsersEntity user) {
        return userAlertRepository.findAllByUsers(user);
    }

    @Transactional
    public void readAlert(UserAlertEntity alert) {
        alert.readAlert();
    }

    public UserAlertEntity findByIdWithThrow(Long alertId) {
        return userAlertRepository.findById(alertId)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND));
    }

    @Transactional
    public void delete(UserAlertEntity entity) {
        userAlertRepository.delete(entity);
    }
}
