package aba3.lucid.domain.payment.converter;

import aba3.lucid.common.annotation.Converter;
import aba3.lucid.domain.cart.dto.CartDetailResponse;
import aba3.lucid.domain.payment.entity.PayDetailEntity;
import aba3.lucid.domain.payment.entity.PayDetailOptionEntity;

import java.util.List;

@Converter
public class CartToPaymentConverter {

    public List<PayDetailOptionEntity> toEntityList(List<CartDetailResponse> response, PayDetailEntity payDetail) {
        return response.stream()
                .map(it -> toEntity(it, payDetail))
                .toList()
                ;
    }

    public PayDetailOptionEntity toEntity(CartDetailResponse response, PayDetailEntity payDetail) {
        return PayDetailOptionEntity.builder()
                .payDetail(payDetail)
                .payOpName(response.getOptionName())
                .payOpDtName(response.getOptionDetailName())
                .payOpTaskTime(response.getDetailTaskTime())
                .payDtQuantity(response.getQuantity())
                .payOpPlusCost(response.getDetailPrice())
                .build()
                ;
    }

}
