package aba3.lucid.domain.product.dress.convertor;

import aba3.lucid.common.annotation.Converter;
import aba3.lucid.domain.product.convertor.ConverterIfs;
import aba3.lucid.domain.product.dress.entity.DressEntity;
import aba3.lucid.domain.product.dress.dto.DressRequest;
import aba3.lucid.domain.product.dress.dto.DressResponse;

@Converter
public class DressConverter implements ConverterIfs<DressEntity, DressRequest, DressResponse> {

    @Override
    public DressResponse toResponse(DressEntity dressEntity) {
        return null;
    }

    @Override
    public DressEntity toEntity(DressRequest dressDto) {
        return null;
    }

}
