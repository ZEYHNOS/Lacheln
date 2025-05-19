package aba3.lucid.blockschedule.controller;

import aba3.lucid.common.api.API;
import aba3.lucid.domain.schedule.dto.*;
import aba3.lucid.blockschedule.business.BlockScheduleBusiness;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/blockschedule")
public class BlockScheduleController {


    private final BlockScheduleBusiness blockScheduleBusiness;

    // 결제 완료 후 블락 일정 테이블에 데이터 추가
    @PostMapping("/add")
    @Operation(summary = "블락 일정 데이터 추가", description = "블락 일정 데이터를 추가합니다.")
    public API<BlockScheduleDto> addSchedule(
            BlockScheduleAddRequest request
    )   {
        return blockScheduleBusiness.addSchedule(request);
    }
    
    // 환불 후 Block 일정 테이블에 데이터 제거
    @DeleteMapping
    @Operation(summary = "블락 일정 데이터 제거", description = "블락 일정 데이터를 제거합니다.")
    public API<BlockScheduleDto> deleteSchedule(
            BlockScheduleDeleteRequest request
    ) {
        return blockScheduleBusiness.deleteSchedule(request);
    }

    // 특정 상품에 대한 Block 일정 데이터 불러오기
    @GetMapping
    @Operation(summary = "블락 일정 데이터 불러오기",  description = "블락 일정 데이터들을 불러옵니다.")
    public API<BlockScheduleListResponse> getSchedules(
            BlockScheduleListRequest request
    ) {
        return blockScheduleBusiness.getSchedules(request);
    }
}
