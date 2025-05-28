package aba3.lucid.domain.payment.convertor;

import aba3.lucid.common.annotation.Converter;
import aba3.lucid.domain.payment.dto.PayDetailBlockResponse;
import aba3.lucid.domain.payment.dto.PayDetailOptionResponse;
import aba3.lucid.domain.payment.dto.PayDetailRequest;
import aba3.lucid.domain.payment.dto.PayDetailResponse;
import aba3.lucid.domain.payment.entity.PayDetailEntity;
import aba3.lucid.domain.payment.entity.PayManagementEntity;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Converter
@RequiredArgsConstructor
public class PayDetailConverter {

    private final PayDetailOptionConverter payDetailOptionConverter;

    public PayDetailEntity toEntity(PayDetailRequest request, PayManagementEntity payManagement) {
        PayDetailEntity entity = PayDetailEntity.builder()
                .payManagement(payManagement)
                .cpId(request.getCpId())
                .couponId(request.getCouponBoxId())
                .productName(request.getProductName())
                .payCost(request.getPayCost())
                .payDcPrice(request.getPayDcPrice())
                .startDatetime(request.getScheduleDate())
                .build()
                ;

        entity.updatePayDetailOptionEntity(payDetailOptionConverter.toEntityList(request.getPayDetailOptionEntityList(), entity));
        return entity;
    }

    public List<PayDetailEntity> toEntityList(List<PayDetailRequest> requestList, PayManagementEntity payManagement) {
        return requestList.stream()
                .map(it -> toEntity(it, payManagement))
                .toList()
                ;
    }

    public PayDetailResponse toResponse(PayDetailEntity entity) {
        return PayDetailResponse.builder()
                .payDetailId(entity.getPayDetailId())
                .cpId(entity.getCpId())
                .couponId(entity.getCouponId())
                .productName(entity.getProductName())
                .payCost(entity.getPayCost())
                .payDcPrice(entity.getPayDcPrice())
                .options(payDetailOptionConverter.toResponseList(entity.getPayDetailOptionEntityList()))
                .build();
    }

    public List<PayDetailResponse> toResponseList(List<PayDetailEntity> entityList) {
        return entityList.stream()
                .map(this::toResponse)
                .toList();
    }

    public PayDetailBlockResponse toBlockResponse(PayDetailEntity entity) {
        return PayDetailBlockResponse.builder()
                .pdId(entity.getPdId())
                .startTime(entity.getStartDatetime())
                .taskTime(entity.getTaskTime())
                .options(payDetailOptionConverter.toBlockResponseList(entity.getPayDetailOptionEntityList()))
                .build();
    }

    public List<PayDetailBlockResponse> toBlockResponseList(List<PayDetailEntity> entityList) {
        return entityList.stream()
                .map(this::toBlockResponse)
                .toList();
    }
}
