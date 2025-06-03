package aba3.lucid.alert;

import aba3.lucid.domain.user.entity.UserAlertEntity;
import aba3.lucid.domain.user.repository.UserAlertRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserAlertService {

    private final UserAlertRepository userAlertRepository;

    public void alertRegister(UserAlertEntity entity) {
        userAlertRepository.save(entity);
    }

}
