package aba3.lucid.domain.product.dress.convertor;

import aba3.lucid.common.annotation.Converter;
import aba3.lucid.common.ifs.ConverterIfs;
import aba3.lucid.domain.product.dress.entity.DressEntity;
import aba3.lucid.domain.product.dress.dto.DressRequest;
import aba3.lucid.domain.product.dress.dto.DressResponse;

@Converter
public class DressConverter implements ConverterIfs<DressEntity, DressRequest, DressResponse> {

    @Override
    public DressResponse toResponse(DressEntity entity) {
        if (entity == null) {
            return null;
        }

        return DressResponse.builder().build();
    }

    @Override
    public DressEntity toEntity(DressRequest req) {
        if (req == null) {
            return null;
        }

        return DressEntity.builder().build();
    }
}
