package aba3.lucid.domain.payment.converter;

import aba3.lucid.common.annotation.Converter;
import aba3.lucid.domain.cart.dto.CartDetailResponse;
import aba3.lucid.domain.company.enums.CompanyCategory;
import aba3.lucid.domain.payment.dto.PayDetailBlockOptionResponse;
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

    public PayDetailBlockOptionResponse toBlockResponse(PayDetailOptionEntity entity) {
        return PayDetailBlockOptionResponse.builder()
                .payOpTaskTime(entity.getPayOpTaskTime())
                .build();
    }

    public List<PayDetailBlockOptionResponse> toBlockResponseList(List<PayDetailOptionEntity> entityList) {
        return entityList.stream()
                .map(this::toBlockResponse)
                .toList()
                ;
    }

    public PayDetailOptionEntity toPayDetailOptionEntityByCartDetailResponse(CartDetailResponse response, PayDetailEntity entity) {
        PayDetailOptionEntity payDetailOptionEntity = PayDetailOptionEntity.builder()
                .payDetail(entity)
                .payOpName(response.getOptionName())
                .payOpDtName(response.getOptionDetailName())
                .payOpTaskTime(response.getDetailTaskTime())
                .payDtQuantity(response.getQuantity())
                .payOpPlusCost(response.getDetailPrice())
                .build()
                ;

        // TODO 드레스 상품 등록할 때 옵션 이름이 사이즈일 때 막기(예약어로 사용)
        if (entity.getCategory().equals(CompanyCategory.D) && response.getOptionName().equals("사이즈")) {
            payDetailOptionEntity.updateDressSize(response.getOptionDetailName());
        }

        return payDetailOptionEntity;
    }

    public List<PayDetailOptionEntity> toPayDetailOptionEntityListByCartDetailResponseList(List<CartDetailResponse> cartDetailResponseList, PayDetailEntity entity) {
        return cartDetailResponseList.stream()
                .map(it -> toPayDetailOptionEntityByCartDetailResponse(it, entity))
                .toList()
                ;
    }
}
