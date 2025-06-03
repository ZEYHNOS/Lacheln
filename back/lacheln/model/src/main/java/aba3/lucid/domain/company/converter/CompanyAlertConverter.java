package aba3.lucid.domain.company.converter;

import aba3.lucid.common.annotation.Converter;
import aba3.lucid.domain.alert.dto.CompanyAlertDto;
import aba3.lucid.domain.alert.dto.MutualAlert;
import aba3.lucid.domain.alert.entity.CompanyAlertEntity;
import aba3.lucid.domain.company.entity.CompanyEntity;

@Converter
public class CompanyAlertConverter {

    public CompanyAlertEntity toEntity(MutualAlert dto, CompanyEntity company) {
        return CompanyAlertEntity.builder()
                .company(company)
                .cpAlertTitle(dto.getTitle())
                .cpAlertContent(dto.getContent())
                .cpAlertSendTime(dto.getSentTime())
                .cpAlertAccessUrl(dto.getAccessUrl())
                .cpAlertRead(dto.getIsRead())
                .build()
                ;
    }

    public CompanyAlertEntity toEntity(CompanyAlertDto dto, CompanyEntity company) {
        return CompanyAlertEntity.builder()
                .company(company)
                .cpAlertTitle(dto.getTitle())
                .cpAlertContent(dto.getContent())
                .cpAlertSendTime(dto.getSentTime())
                .cpAlertAccessUrl(dto.getAccessUrl())
                .cpAlertRead(dto.getIsRead())
                .build()
                ;
    }

    public MutualAlert toDto(CompanyAlertEntity entity) {
        return MutualAlert.builder()
                .title(entity.getCpAlertTitle())
                .content(entity.getCpAlertContent())
                .sentTime(entity.getCpAlertSendTime())
                .accessUrl(entity.getCpAlertAccessUrl())
                .isRead(entity.getCpAlertRead())
                .build()
                ;
    }

}
