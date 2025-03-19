package aba3.lucid.domain.product.makeup.convertor;

import aba3.lucid.common.annotation.Converter;
import aba3.lucid.common.ifs.ConverterIfs;
import aba3.lucid.domain.company.entity.CompanyEntity;
import aba3.lucid.domain.product.makeup.dto.MakeUpResponse;
import aba3.lucid.domain.product.makeup.dto.MakeupRequest;
import aba3.lucid.domain.product.makeup.entity.MakeupEntity;

@Converter
public class MakeUpConverter implements ConverterIfs<MakeupEntity, MakeupRequest, MakeUpResponse> {


    @Override
    public MakeUpResponse toResponse(MakeupEntity makeupEntity) {
        return null;
    }

    @Override
    public MakeupEntity toEntity(MakeupRequest makeupRequest, CompanyEntity entity) {
        return null;
    }

}
