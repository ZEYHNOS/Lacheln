package aba3.lucid.payment.service;

import aba3.lucid.common.exception.ApiException;
import aba3.lucid.common.status_code.ErrorCode;
import aba3.lucid.domain.cart.dto.CartDetailRequest;
import aba3.lucid.domain.cart.dto.CartRequest;
import aba3.lucid.domain.payment.entity.PayDetailEntity;
import aba3.lucid.domain.payment.enums.PaymentStatus;
import aba3.lucid.domain.payment.repository.PayDetailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PayDetailService {

    private final PayDetailRepository payDetailRepository;

    public List<PayDetailEntity> getPayDetailList(Long companyId) {
        return payDetailRepository.findAllByCpId(companyId);
    }

    public List<PayDetailEntity> getPayDetailList(Long pdId, LocalDateTime start, LocalDateTime end)    {
        return payDetailRepository.findByPdIdAndDateTime(pdId, start, end);
    }

    // 시간대로 예약 여부 조회
    public void checkReservation(List<CartRequest> requests)    {

        // 카트 요청 리스트에서 CartRequest에 있는 TaskTime 더하기
        for (CartRequest request : requests) {
            Long pdId = request.getPdId();
            LocalTime taskTime = request.getPdTaskTime();
            LocalDateTime start = request.getStartDateTime();
            LocalDateTime end = start
                    .plusHours(taskTime.getHour())
                    .plusMinutes(taskTime.getMinute());
            // 하나의 CartRequest에 있는 CartDetailRequest의 TaskTime 더하기
            for(CartDetailRequest detailRequest : request.getPdDetails())   {
                LocalTime dtTaskTime = detailRequest.getOpTaskTime();
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
        return payDetailRepository.findAllByStartDateTimeBetweenAndPayManagement_PayStatus(
                LocalDateTime.now().minusDays(1),
                LocalDateTime.now(),
                PaymentStatus.PAID
        );
    }
}
