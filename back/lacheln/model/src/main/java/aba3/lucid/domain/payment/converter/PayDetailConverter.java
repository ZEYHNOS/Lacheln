package aba3.lucid.domain.payment.converter;

import aba3.lucid.common.annotation.Converter;
import aba3.lucid.domain.cart.dto.CartAllResponse;
import aba3.lucid.domain.company.enums.CompanyCategory;
import aba3.lucid.domain.payment.dto.PayDetailBlockResponse;
import aba3.lucid.domain.payment.dto.PayDetailOptionRequest;
import aba3.lucid.domain.payment.dto.PayDetailRequest;
import aba3.lucid.domain.payment.dto.PayDetailResponse;
import aba3.lucid.domain.payment.entity.PayDetailEntity;
import aba3.lucid.domain.payment.entity.PayDetailOptionEntity;
import aba3.lucid.domain.payment.entity.PayManagementEntity;
import aba3.lucid.domain.product.dto.option.OptionDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Converter
@RequiredArgsConstructor
public class PayDetailConverter {

    private final PayDetailOptionConverter payDetailOptionConverter;
    private final CartToPaymentConverter cartToPaymentConverter;

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
                .payCost(entity.getPayDcPrice())
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
                .cpId(response.getCpId())
                .category(response.getCategory())
                .build()
                ;

        log.info("payDetailConverter : entity {}", entity);
        entity.updatePayDetailOptionEntity(cartToPaymentConverter.toEntityList(response.getCartDetails(), entity));

        if (response.getCategory().equals(CompanyCategory.M)) {
            entity.updateManager(response.getManager());
        }

        return entity;
    }

    public List<OptionDto> toDtoList(List<PayDetailOptionEntity> payDetailOptionEntityList) {
        Map<String, List<OptionDto.OptionDetailDto>> map = new HashMap<>();
        for (PayDetailOptionEntity payDetailOption : payDetailOptionEntityList) {
            String key = payDetailOption.getPayOpName();
            OptionDto.OptionDetailDto value = OptionDto.OptionDetailDto.builder()
                    .opDtName(payDetailOption.getPayOpDtName())
                    .quantity(payDetailOption.getPayDtQuantity())
                    .build()
                    ;

            map.getOrDefault(key, new ArrayList<>()).add(value);
        }

        List<OptionDto> result = new ArrayList<>();
        for (Map.Entry<String, List<OptionDto.OptionDetailDto>> entrySet : map.entrySet()) {
            result.add(OptionDto.builder()
                            .name(entrySet.getKey())
                            .optionDtList(entrySet.getValue())
                    .build()
            );
        }

        return result;
    }
}
