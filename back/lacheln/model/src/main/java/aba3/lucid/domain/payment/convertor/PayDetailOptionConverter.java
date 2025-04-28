package aba3.lucid.domain.payment.convertor;

import aba3.lucid.common.annotation.Converter;
import aba3.lucid.domain.payment.dto.PayDetailOptionRequest;
import aba3.lucid.domain.payment.dto.PayDetailOptionResponse;
import aba3.lucid.domain.payment.entity.PayDetailEntity;
import aba3.lucid.domain.payment.entity.PayDetailOptionEntity;

import java.util.List;

@Converter
public class PayDetailOptionConverter {

    public PayDetailOptionEntity toEntity(PayDetailOptionRequest request, PayDetailEntity payDetail) {
        return PayDetailOptionEntity.builder()
                .payDetail(payDetail)
                .payOpName(request.getPdOpName())
                .payOpDtName(request.getPdOpDtName())
                .payDtQuantity(request.getQuantity())
                .payOpPlusCost(request.getPayOpPlusCost())
                .build()
                ;
    }

    public List<PayDetailOptionEntity> toEntityList(List<PayDetailOptionRequest> requestList, PayDetailEntity payDetail) {
        return requestList.stream()
                .map(it -> toEntity(it, payDetail))
                .toList()
                ;
    }

    public PayDetailOptionResponse toResponse(PayDetailOptionEntity entity) {
        return PayDetailOptionResponse.builder()
                .payDetailOptionId(entity.getPayDetailOptionId())
                .payOpName(entity.getPayOpName())
                .payOpDtName(entity.getPayOpDtName())
                .payDtQuantity(entity.getPayDtQuantity())
                .payOpPlusCost(entity.getPayOpPlusCost())
                .build();
    }

    public List<PayDetailOptionResponse> toResponseList(List<PayDetailOptionEntity> entityList) {
        return entityList.stream()
                .map(this::toResponse)
                .toList();
    }
}
