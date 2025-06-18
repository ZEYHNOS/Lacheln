package aba3.lucid.payment.service;

import aba3.lucid.common.exception.ApiException;
import aba3.lucid.common.status_code.ErrorCode;
import aba3.lucid.domain.cart.dto.CartAddProductRequest;
import aba3.lucid.domain.cart.dto.CartOptionDetail;
import aba3.lucid.domain.payment.dto.PopularDto;
import aba3.lucid.domain.payment.entity.PayDetailEntity;
import aba3.lucid.domain.payment.enums.PaymentStatus;
import aba3.lucid.domain.payment.repository.PayDetailRepository;
import aba3.lucid.domain.product.entity.PopularEntity;
import aba3.lucid.domain.user.entity.UsersEntity;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PayDetailService {

    private final RabbitTemplate rabbitTemplate;
    private final PayDetailRepository payDetailRepository;

    public List<PayDetailEntity> getPayDetailList(Long companyId) {
        return payDetailRepository.findAllByCpId(companyId);
    }

    public List<PayDetailEntity> getPayDetailList(UsersEntity user) {
        return payDetailRepository.findAllByPayManagement_User(user);
    }

    public List<PayDetailEntity> getPayDetailList(Long pdId, LocalDateTime start, LocalDateTime end)    {
        return payDetailRepository.findByPdIdAndDateTime(pdId, start, end);
    }

    // 시간대로 예약 여부 조회
    public void checkReservation(List<CartAddProductRequest> requests)    {

        // 카트 요청 리스트에서 CartRequest에 있는 TaskTime 더하기
        for (CartAddProductRequest request : requests) {
            Long pdId = request.getPdId();
            LocalTime taskTime = request.getTaskTime();
            LocalDateTime start = request.getStartDatetime();
            LocalDateTime end = start
                    .plusHours(taskTime.getHour())
                    .plusMinutes(taskTime.getMinute());
            // 하나의 CartRequest에 있는 CartDetailRequest의 TaskTime 더하기
            for (CartOptionDetail detailRequest : request.getOptionDetails())   {
                LocalTime dtTaskTime = detailRequest.getOpTasktime();
                end = end
                        .plusHours(dtTaskTime.getHour())
                        .plusMinutes(dtTaskTime.getMinute());
            }
            
            // 그렇게 구한 Start, End Time으로 이미 결제완료 된(예약된)일정이 있는지 확인
            if(payDetailRepository.existsByPdIdAndDateTimes(pdId, start, end))  {
                throw new ApiException(ErrorCode.IT_ALREADY_EXISTS, "이미 있는 일정 입니다.");
            }
        }
    }

    // 리뷰를 적어야 하는 유저 리스트
    public List<PayDetailEntity> replyNeedUserList() {
        return payDetailRepository.findAllByStartDatetimeBetweenAndPayManagement_PayStatus(
                LocalDateTime.now().minusMinutes(1),
                LocalDateTime.now(),
                PaymentStatus.PAID
        );
    }

    public void createPopularProductList() throws JsonProcessingException {
        LocalDateTime start = LocalDateTime.now().minusDays(7);
        LocalDateTime end = LocalDateTime.now();
        List<Long> popularPdIdList = payDetailRepository.findTop10BestSellingPdIds(start, end);
        log.info("popular id list : {}", popularPdIdList);
        List<PayDetailEntity> payDetailEntityList = payDetailRepository.findAllByPdIdIn(popularPdIdList);
        log.info("paydetailEntityList : {}", payDetailEntityList);
        List<PopularDto> dtoList = new ArrayList<>();
        for (int i = 1; i <= payDetailEntityList.size(); i++) {
            PayDetailEntity payDetail = payDetailEntityList.get(i-1);
            dtoList.add(PopularDto.builder()
                    .productId(payDetail.getPdId())
                    .companyId(payDetail.getCpId())
                    .rank(i)
                    .build()
            );
        }

        log.info("payDtoList : {}", dtoList);
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(dtoList);
        log.info("producer json : {}", json);
        rabbitTemplate.convertAndSend("product.exchange", "popular", json);
    }
}
