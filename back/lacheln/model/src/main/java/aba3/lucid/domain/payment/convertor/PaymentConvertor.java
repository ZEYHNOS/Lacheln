package aba3.lucid.domain.payment.convertor;

import aba3.lucid.common.annotation.Converter;
import aba3.lucid.domain.payment.dto.PayManagementResponse;
import aba3.lucid.domain.payment.dto.PaymentRequest;
import aba3.lucid.domain.payment.entity.PayManagementEntity;
import aba3.lucid.domain.user.entity.UsersEntity;
import lombok.RequiredArgsConstructor;

@Converter
@RequiredArgsConstructor
public class PaymentConvertor {

    private final PayDetailConverter payDetailConverter;

    public PayManagementEntity toEntity(PaymentRequest request, UsersEntity user) {
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

        entity.updatePayDetailEntityList(payDetailConverter.toEntityList(request.getPayDetailRequestList(), entity));
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

}
