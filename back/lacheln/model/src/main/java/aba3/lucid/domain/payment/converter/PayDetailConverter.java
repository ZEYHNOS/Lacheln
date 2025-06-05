package aba3.lucid.domain.payment.converter;

import aba3.lucid.common.annotation.Converter;
import aba3.lucid.domain.cart.dto.CartAllResponse;
import aba3.lucid.domain.cart.dto.CartDetailResponse;
import aba3.lucid.domain.cart.entity.CartEntity;
import aba3.lucid.domain.company.enums.CompanyCategory;
import aba3.lucid.domain.payment.dto.PayDetailBlockResponse;
import aba3.lucid.domain.payment.dto.PayDetailOptionRequest;
import aba3.lucid.domain.payment.dto.PayDetailRequest;
import aba3.lucid.domain.payment.dto.PayDetailResponse;
import aba3.lucid.domain.payment.entity.PayDetailEntity;
import aba3.lucid.domain.payment.entity.PayDetailOptionEntity;
import aba3.lucid.domain.payment.entity.PayManagementEntity;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Converter
@RequiredArgsConstructor
public class PayDetailConverter {

    private final PayDetailOptionConverter payDetailOptionConverter;

    public PayDetailEntity toEntity(PayDetailRequest request, PayManagementEntity payManagement) {
        LocalDateTime endDateTime = request.getScheduleDate()
                .plusHours(request.getTaskTime().getHour())
                .plusMinutes(request.getTaskTime().getMinute())
                ;

        for(PayDetailOptionRequest optionRequest : request.getPayDetailOptionEntityList()) {
            endDateTime = endDateTime.plusHours(optionRequest.getTaskTime().getHour());
            endDateTime = endDateTime.plusMinutes(optionRequest.getTaskTime().getMinute());
        }

        PayDetailEntity entity = PayDetailEntity.builder()
                .payManagement(payManagement)
                .cpId(request.getCpId())
                .pdId(request.getPdId())
                .productName(request.getProductName())
                .imageUrl(request.getImageUrl())
                .couponName(request.getCouponName())
                .payCost(request.getPayCost())
                .payDcPrice(request.getPayDcPrice())
                .startDatetime(request.getScheduleDate())
                .endDatetime(endDateTime)
                .taskTime(request.getTaskTime())
                .category(request.getCategory())
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
                .userId(entity.getPayManagement().getUser().getUserId())
                .userName(entity.getPayManagement().getUser().getUserName())
                .cpId(entity.getCpId())
                .couponName(entity.getCouponName())
                .pdName(entity.getProductName())
                .payCost(entity.getPayCost().subtract(entity.getPayDcPrice()))
                .status(entity.getPayManagement().getPayStatus())
                .paidAt(entity.getPayManagement().getPaidAt())
                .refundPrice(entity.getPayManagement().getPayRefundPrice())
                .scheduleAt(entity.getStartDatetime())
                .category(entity.getCategory())
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
                .endTime(entity.getEndDatetime())
                .build();
    }

    public List<PayDetailBlockResponse> toBlockResponseList(List<PayDetailEntity> entityList) {
        return entityList.stream()
                .map(this::toBlockResponse)
                .toList();
    }

    public List<PayDetailEntity> toPayDetailEntityListByCartEntityList(PayManagementEntity payManagement, List<CartAllResponse> cartAllResponseList) {
        return cartAllResponseList.stream()
                .map(it -> toPayDetailEntityByCartEntity(payManagement, it))
                .toList()
                ;
    }

    public PayDetailEntity toPayDetailEntityByCartEntity(PayManagementEntity payManagement, CartAllResponse response) {
        LocalDateTime endDateTime = response.getStartTime()
                .plusHours(response.getTaskTime().getHour())
                .plusMinutes(response.getTaskTime().getMinute())
                ;

        PayDetailEntity entity = PayDetailEntity.builder()
                .payManagement(payManagement)
                .productName(response.getPdName())
                .pdId(response.getPdId())
                .payDetailOptionEntityList(null)
                .payCost(response.getPrice())
                .startDatetime(response.getStartTime())
                .endDatetime(endDateTime)
                .taskTime(response.getTaskTime())
                .imageUrl(response.getPdImageUrl())
                .couponName(null)
//                .category(response.getCategory()) // TODO 장바구니에 생기면 담기
                .build()
                ;

//        if (response.getCategory().equals(CompanyCategory.M)) {
//            endDateTime.updateManager(response.getManager());
//        }

        return entity;
    }
}
