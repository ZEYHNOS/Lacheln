package aba3.lucid.blockschedule.service;

import aba3.lucid.domain.schedule.entity.BlockScheduleEntity;
import aba3.lucid.domain.schedule.repository.BlockScheduleRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BlockScheduleService {

    private final BlockScheduleRepository blockScheduleRepository;

    // 생성한 엔티티 저장
    public BlockScheduleEntity save(BlockScheduleEntity schedule) {
        return blockScheduleRepository.save(schedule);
    }

    // 상품 ID, 시작시간, 종료시간을 통해 block 스케쥴 조회
    public List<BlockScheduleEntity> getSchedulesByPdIdAndDate(LocalDateTime startTime, LocalDateTime endTime, Long pdId)  {
        return blockScheduleRepository.findByPdIdAndDate(pdId, startTime, endTime);
    }

    // 이미 있는 일정인지 조회하는 메서드
    public boolean checkBlockSchedule(Long pdId, LocalDateTime startTime) {
        List<BlockScheduleEntity> schedules = blockScheduleRepository.findByPdIdAndStartTime(pdId, startTime);
        return schedules.isEmpty();
    }

    // 상품ID, 시작시간, 종료시간으로 찾아 삭제
    @Transactional
    public void deleteByPdIdAndDate(Long pdId, LocalDateTime startTime, LocalDateTime endTime) {
        blockScheduleRepository.deleteByPdIdAndDateTime(pdId, startTime, endTime);
    }
}
