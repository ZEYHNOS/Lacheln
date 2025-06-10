package aba3.lucid.domain.payment.converter;

import aba3.lucid.common.annotation.Converter;
import aba3.lucid.domain.cart.dto.CartAllResponse;
import aba3.lucid.domain.payment.dto.PayManagementResponse;
import aba3.lucid.domain.payment.dto.PaymentRequest;
import aba3.lucid.domain.payment.entity.PayDetailEntity;
import aba3.lucid.domain.payment.entity.PayManagementEntity;
import aba3.lucid.domain.user.entity.UsersEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@Converter
@RequiredArgsConstructor
public class PaymentConvertor {

    private final PayDetailConverter payDetailConverter;

    public PayManagementEntity toEntity(PaymentRequest request, UsersEntity user, List<CartAllResponse> cartAllResponseList) {
        PayManagementEntity entity = PayManagementEntity.builder()
                .payId(request.getPayId())
                .user(user)
                .payTool(request.getPayTool())
                .payTotalPrice(request.getPayTotalPrice())
                .payDcPrice(request.getPayDcPrice())
                .payStatus(request.getPayStatus())
                .payMileage(request.getPayMileage())
                .payImpUid(request.getPayImpUid())
                .paidAt(request.getPaidAt())
                .build()
                ;

        entity.updatePayDetailEntityList(payDetailConverter.toPayDetailEntityListByCartEntityList(entity, cartAllResponseList));
        return entity;
    }

    public PayManagementResponse toResponse(PayManagementEntity entity) {
        return PayManagementResponse.builder()
                .payId(entity.getPayId())
                .userId(entity.getUser().getUserId())
                .payTool(entity.getPayTool())
                .payTotalPrice(entity.getPayTotalPrice())
                .payDcPrice(entity.getPayDcPrice())
                .payStatus(entity.getPayStatus())
                .payRefundPrice(entity.getPayRefundPrice())
                .payRefundDate(entity.getPayRefundDate())
                .payMileage(entity.getPayMileage())
                .payImpUid(entity.getPayImpUid())
                .paidAt(entity.getPaidAt())
                .payDetails(payDetailConverter.toResponseList(entity.getPayDetailEntityList()))
                .build();
    }

    public List<PayManagementResponse> toResponseList(List<PayManagementEntity> entityList) {
        return entityList.stream()
                .map(this::toResponse)
                .toList()
                ;
    }

}
