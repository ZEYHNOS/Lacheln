package aba3.lucid.blockschedule.business;

import aba3.lucid.common.annotation.Business;
import aba3.lucid.common.api.API;
import aba3.lucid.common.exception.ApiException;
import aba3.lucid.common.status_code.ErrorCode;
import aba3.lucid.domain.schedule.convertor.BlockScheduleConvertor;
import aba3.lucid.domain.schedule.dto.*;
import aba3.lucid.domain.schedule.entity.BlockScheduleEntity;
import aba3.lucid.blockschedule.service.BlockScheduleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.core.Block;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Business
@RequiredArgsConstructor
public class BlockScheduleBusiness {

    private final BlockScheduleService blockScheduleService;
    private final BlockScheduleConvertor blockScheduleConvertor;

    // 상품ID, 년, 월, 일로 Block 일정을 조회
    public API<BlockScheduleListResponse> getSchedules(BlockScheduleListRequest request) {
        if(request == null) {
            throw new ApiException(ErrorCode.BAD_REQUEST);
        }

        // 요청한 LocalDate타입의 값을 해당 일자의 시작, 끝으로 변경
        LocalDate getDate = request.getSearchDate();
        LocalDateTime getStartTime = getDate.atStartOfDay();
        LocalDateTime getEndTime = getDate.atTime(23, 59, 59, 999_999_999);

        // 해당하는 일자의 시작, 끝에 해당하는 상품ID가 포함된 일정을 전부 추출
        List<BlockScheduleEntity> schedules = blockScheduleService.getSchedulesByPdIdAndDate(getStartTime, getEndTime, request.getPdId());

        return API.OK(BlockScheduleListResponse.builder().blockSchedules(blockScheduleConvertor.convertToDto(schedules)).build());
    }

    // 상품ID, 년, 월, 일, 시간, 분으로 Block 일정추가
    public API<BlockScheduleDto> addSchedule(BlockScheduleAddRequest request) {
        if(request == null) {
            throw new ApiException(ErrorCode.BAD_REQUEST);
        }
        
        // 날짜 파싱
        LocalDateTime start = request.getStartTime();
        LocalDateTime end = start.plusMinutes(request.getTaskTime());
        
        // 이미 있는지 확인
        if(!blockScheduleService.checkBlockSchedule(request.getPdId(), start)) {
            throw new ApiException(ErrorCode.IT_ALREADY_EXISTS);
        }
        
        // 엔티티 생성
        BlockScheduleEntity blockSchedule = BlockScheduleEntity.builder()
                .pdId(request.getPdId())
                .startTime(start)
                .endTime(end)
                .build();

        // 저장
        blockScheduleService.save(blockSchedule);

        // 반환할 응답 생성
        BlockScheduleDto response = BlockScheduleDto.builder()
                .startTime(request.getStartTime())
                .pdId(request.getPdId())
                .endTime(end)
                .build();

        return API.OK(response, "일정 저장에 성공하였습니다.");
    }

    // 상품ID, 년, 월, 일, 시간, 분으로 Block 일정삭제
    public API<BlockScheduleDto> deleteSchedule(BlockScheduleDeleteRequest request) {
        if(request == null) {
            throw new ApiException(ErrorCode.BAD_REQUEST);
        }

        LocalDateTime end = request.getStartTime().plusMinutes(request.getTaskTime());
        
        blockScheduleService.deleteByPdIdAndDate(request.getPdId(), request.getStartTime(), end);

        BlockScheduleDto response = BlockScheduleDto.builder()
                .pdId(request.getPdId())
                .startTime(request.getStartTime())
                .endTime(end)
                .build();

        return API.OK(response, "일정이 성공적으로 삭제되었습니다!");
    }
}
