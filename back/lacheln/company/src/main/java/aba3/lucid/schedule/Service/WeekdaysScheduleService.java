package aba3.lucid.schedule.Service;


import aba3.lucid.common.enums.Weekdays;
import aba3.lucid.common.exception.ApiException;
import aba3.lucid.common.status_code.ErrorCode;
import aba3.lucid.domain.company.entity.CompanyEntity;
import aba3.lucid.domain.company.repository.CompanyRepository;
import aba3.lucid.domain.schedule.convertor.WeekdaysScheduleConvertor;
import aba3.lucid.domain.schedule.dto.WeekdaysScheduleRequest;
import aba3.lucid.domain.schedule.dto.WeekdaysScheduleResponse;
import aba3.lucid.domain.schedule.entity.WeekdaysScheduleEntity;
import aba3.lucid.domain.schedule.repository.ScheduleRepository;
import aba3.lucid.domain.schedule.repository.WeekdaysScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WeekdaysScheduleService {
    private final WeekdaysScheduleRepository weekdaysScheduleRepository;
    private final CompanyRepository companyRepository;
    private final WeekdaysScheduleConvertor convertor;
    private final ScheduleRepository scheduleRepository;
    private final WeekdaysScheduleConvertor weekdaysScheduleConvertor;


    @Transactional
    public void saveSchedules(Long cpId, WeekdaysScheduleRequest request) {
        CompanyEntity company = companyRepository.findById(cpId)
                .orElseThrow(()->new ApiException(ErrorCode.NOT_FOUND, "회사를 찾을 수 없습니다."));
        //기존 스케줄 삭제???????????
//        List<WeekdaysScheduleEntity> existingSchedules = weekdaysScheduleRepository.findByCompany_CpId(cpId);
//        if(existingSchedules != null && !existingSchedules.isEmpty()) {
//            weekdaysScheduleRepository.deleteAll(existingSchedules);
//        }

        //요청 DTO를 엔티티 리스트로 반환
        List<WeekdaysScheduleEntity> newSchedules = convertor.toEntityList(request,company);
        weekdaysScheduleRepository.saveAll(newSchedules);

    }

    @Transactional
    public List<WeekdaysScheduleResponse> getSchedules(Long cpId) {
        List<WeekdaysScheduleEntity> entities = weekdaysScheduleRepository.findByCompany_CpId(cpId);
        return convertor.toResponseList(entities);
    }

    public WeekdaysScheduleResponse updateCompanySchedules(Long cpId, WeekdaysScheduleRequest request) {
        CompanyEntity company = companyRepository.findById(cpId)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, "회사를 찾을 없습니다"));
        //해당하는 기존 요일별 스케줄들을 DB에서 조회합니다.
        List<WeekdaysScheduleEntity> existingSchedules = weekdaysScheduleRepository.findByCompany_CpId(cpId);
        Map<Weekdays, WeekdaysScheduleEntity> scheduleMap = existingSchedules.stream()
                .collect(Collectors.toMap(WeekdaysScheduleEntity::getWsWeekdays, Function.identity()));
        // 조회된 스케줄 리스트를 Weekdays(요일)별로 매핑하여 Map으로 변환합니다.
        //Function.identity()는 각 스케줄 엔티티를 값으로 사용합니다

        //request.getScheduleList(): 요청받은 요일별 근무시간 리스트를 순회합니다.
        for (WeekdaysScheduleRequest.DayScheduleDto dto: request.getScheduleList()) {
            Weekdays day = Weekdays.valueOf(dto.getWeekday());
            Weekdays newWeekday = Weekdays.valueOf(dto.getWeekday());
            LocalTime newStart = LocalTime.parse(dto.getStart());
            LocalTime newEnd = LocalTime.parse(dto.getEnd());
        //요청받은 day(요일)가 기존에 존재하는 스케줄에 포함되어 있는지 확인합니다.
            if (scheduleMap.containsKey(day)) {
                WeekdaysScheduleEntity schedule = scheduleMap.get(day);
                schedule.setWsWeekdays(newWeekday);
                schedule.setWsStart(newStart);
                schedule.setWsEnd(newEnd);

            }else {
                WeekdaysScheduleEntity newSchedule = WeekdaysScheduleEntity.builder()
                        .company(company)
                        .wsWeekdays(newWeekday)
                        .wsStart(newStart)
                        .wsEnd(newEnd).build();
                weekdaysScheduleRepository.save(newSchedule);
            }
        }
        List<WeekdaysScheduleEntity> updateSchedules = weekdaysScheduleRepository.findByCompany_CpId(cpId);
        return (WeekdaysScheduleResponse) weekdaysScheduleConvertor.toResponseList(updateSchedules);

    }


}
