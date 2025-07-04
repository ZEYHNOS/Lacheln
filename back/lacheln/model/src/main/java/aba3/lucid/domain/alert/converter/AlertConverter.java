package aba3.lucid.domain.alert.converter;

import aba3.lucid.common.annotation.Converter;
import aba3.lucid.domain.alert.dto.CompanyAlertDto;
import aba3.lucid.domain.alert.dto.MutualAlert;
import aba3.lucid.domain.alert.dto.UserAlertDto;
import aba3.lucid.domain.alert.entity.CompanyAlertEntity;
import aba3.lucid.domain.company.entity.CompanyEntity;
import aba3.lucid.domain.user.entity.UserAlertEntity;
import aba3.lucid.domain.user.entity.UsersEntity;

import java.util.List;

@Converter
public class AlertConverter {

    public CompanyAlertEntity toEntity(MutualAlert dto, CompanyEntity company) {
        return CompanyAlertEntity.builder()
                .company(company)
                .cpAlertType(dto.getType())
                .cpAlertText(dto.getText())
                .cpAlertSendTime(dto.getSentTime())
                .cpAlertAccessUrl(dto.getAccessUrl())
                .cpAlertRead(dto.getIsRead())
                .build()
                ;
    }

    public UserAlertEntity toEntity(UserAlertDto dto, UsersEntity user) {
        return UserAlertEntity.builder()
                .users(user)
                .userAlertTitle(dto.getTitle())
                .userAlertContent(dto.getContent())
                .userAlertSendtime(dto.getSentTime())
                .userAlertRead(dto.getIsRead())
                .userAlertUrl(dto.getAccessUrl())
                .build()
                ;
    }

    public CompanyAlertEntity toEntity(CompanyAlertDto dto, CompanyEntity company) {
        return CompanyAlertEntity.builder()
                .company(company)
                .cpAlertType(dto.getType())
                .cpAlertText(dto.getText())
                .cpAlertSendTime(dto.getSentTime())
                .cpAlertAccessUrl(dto.getAccessUrl())
                .cpAlertRead(dto.getIsRead())
                .build()
                ;
    }

    public MutualAlert toDto(CompanyAlertEntity entity) {
        return MutualAlert.builder()
                .alertId(entity.getCpAlertId())
                .type(entity.getCpAlertType())
                .text(entity.getCpAlertText())
                .sentTime(entity.getCpAlertSendTime())
                .accessUrl(entity.getCpAlertAccessUrl())
                .isRead(entity.getCpAlertRead())
                .build()
                ;
    }

    public UserAlertDto toDto(UserAlertEntity entity) {
        return UserAlertDto.builder()
                .alertId(entity.getUserAlertId())
                .title(entity.getUserAlertTitle())
                .content(entity.getUserAlertContent())
                .accessUrl(entity.getUserAlertUrl())
                .isRead(entity.getUserAlertRead())
                .sentTime(entity.getUserAlertSendtime())
                .build()
                ;
    }

    public List<UserAlertDto> toDtoList(List<UserAlertEntity> userAlertEntityList) {
        return userAlertEntityList.stream()
                .map(this::toDto)
                .toList()
                ;
    }
}
