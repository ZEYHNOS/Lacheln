package aba3.lucid.schedule.Service;

import aba3.lucid.common.exception.ApiException;
import aba3.lucid.common.status_code.ErrorCode;
import aba3.lucid.domain.schedule.entity.ScheduleEntity;
import aba3.lucid.domain.schedule.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;

    /**
     * 1. 유저가 상품을 결제
     */
    public ScheduleEntity save(ScheduleEntity schedule) {
        return scheduleRepository.save(schedule);
    }

    public ScheduleEntity update(ScheduleEntity schedule) {
        return scheduleRepository.save(schedule);
    }

    // 결제를 진행하고 있을 때 일정 블락하기
    public void paymentBeforeScheduleBlock() {

    }

    // 해당 날짜 일정 불러오기

    // 업체 전체 일정 불러오기

    // 특정 일정 상세 정보 가지고 오기

    // 특정 시간 일정 상태 확인하기

    //

    @Transactional
    public void deleteById(Long scheduleId) {
        scheduleRepository.deleteById(scheduleId);
    }

    @Transactional
    public void deleteByEntity(ScheduleEntity entity) {
        scheduleRepository.delete(entity);
    }


    public ScheduleEntity findByIdWithThrow(Long scheduleId) {
        return scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND));
    }


}
