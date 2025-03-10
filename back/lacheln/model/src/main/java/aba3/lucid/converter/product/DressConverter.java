package aba3.lucid.converter.product;

import aba3.lucid.common.annotation.Converter;
import aba3.lucid.converter.ConverterIfs;
import aba3.lucid.domain.product.DressEntity;
import aba3.lucid.dto.product.dress.DressRequest;
import aba3.lucid.dto.product.dress.DressResponse;

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
