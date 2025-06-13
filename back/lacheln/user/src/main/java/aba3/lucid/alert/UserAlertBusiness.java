package aba3.lucid.alert;

import aba3.lucid.common.annotation.Business;
import aba3.lucid.domain.alert.converter.AlertConverter;
import aba3.lucid.domain.alert.dto.UserAlertDto;
import aba3.lucid.domain.user.entity.UserAlertEntity;
import aba3.lucid.domain.user.entity.UsersEntity;
import aba3.lucid.user.service.UserService;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Business
@RequiredArgsConstructor
public class UserAlertBusiness {

    private final UserAlertService userAlertService;
    private final AlertConverter alertConverter;
    private final UserService userService;

    public void sentAlert(UserAlertDto dto, UsersEntity user) {
        userAlertService.alertRegister(alertConverter.toEntity(dto, user));
    }

    public List<UserAlertDto> getAlertListByUserId(String userId) {
        UsersEntity user = userService.findByIdWithThrow(userId);
        List<UserAlertEntity> userAlertEntityList = userAlertService.getUserAlertListByUser(user);
        return alertConverter.toDtoList(userAlertEntityList);
    }

    public void readAlertById(Long alertId) {
        UserAlertEntity entity = userAlertService.findByIdWithThrow(alertId);
        userAlertService.readAlert(entity);
    }

    public void deleteAlertById(Long alertId) {
        UserAlertEntity entity = userAlertService.findByIdWithThrow(alertId);
        userAlertService.delete(entity);
    }
}
