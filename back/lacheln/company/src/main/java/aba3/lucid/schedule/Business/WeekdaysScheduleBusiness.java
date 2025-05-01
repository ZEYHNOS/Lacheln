package aba3.lucid.schedule.Business;


import aba3.lucid.common.annotation.Business;
import aba3.lucid.common.enums.Weekdays;
import aba3.lucid.domain.company.entity.CompanyEntity;
import aba3.lucid.domain.company.repository.CompanyRepository;
import aba3.lucid.domain.schedule.convertor.WeekdaysScheduleConvertor;
import aba3.lucid.domain.schedule.dto.WeekdaysScheduleRequest;
import aba3.lucid.domain.schedule.dto.WeekdaysScheduleResponse;
import aba3.lucid.domain.schedule.entity.WeekdaysScheduleEntity;
import aba3.lucid.domain.schedule.repository.TemporaryHolidayRepository;
import aba3.lucid.domain.schedule.repository.WeekdaysScheduleRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Business
@RequiredArgsConstructor
public class WeekdaysScheduleBusiness {
    private final WeekdaysScheduleRepository weekdaysScheduleRepository;
    private final TemporaryHolidayRepository temporaryHolidayRepository;
    private final WeekdaysScheduleConvertor weekdaysScheduleConvertor;
    private final CompanyRepository companyRepository;

    public List<WeekdaysScheduleResponse> createSchedule(WeekdaysScheduleRequest request, Long cpId) {
        CompanyEntity company = companyRepository.findById(cpId).orElseThrow(EntityNotFoundException::new);

        List<WeekdaysScheduleRequest.DayScheduleDto> dtoList = resolveScheduleList(request.getScheduleList());

        List<WeekdaysScheduleEntity> entities = weekdaysScheduleConvertor.toEntity(
                WeekdaysScheduleRequest.builder()
                        .scheduleList(dtoList)
                        .build(),
                company
        );
        //Response 변환
        weekdaysScheduleRepository.saveAll(entities);
        return weekdaysScheduleConvertor.toResponseList(entities);

    }
    private List<WeekdaysScheduleRequest.DayScheduleDto> resolveScheduleList(
            List<WeekdaysScheduleRequest.DayScheduleDto> incoming
    ) {
        if (incoming != null && !incoming.isEmpty()) {
            return incoming;
        }
        return Arrays.stream(Weekdays.values())
                .map(day -> {
                    LocalDateTime start;
                    LocalDateTime end;
                    switch (day) {
                        case SAT:
                            start = LocalDateTime.of(LocalDate.now(), LocalTime.of(10, 0));
                            end   = LocalDateTime.of(LocalDate.now(), LocalTime.of(20, 0));
                            break;
                        case SUN:
                            start = end = null;  // 혹은 별도 휴무 플래그 세팅
                            break;
                        default:
                            start = LocalDateTime.of(LocalDate.now(), LocalTime.of(9, 0));
                            end   = LocalDateTime.of(LocalDate.now(), LocalTime.of(18, 0));
                    }
                    return WeekdaysScheduleRequest.DayScheduleDto.builder()
                            .weekday(day)
                            .start(start)
                            .end(end)
                            .build();
                })
                .collect(Collectors.toList());
    }
    @Transactional
    public List<WeekdaysScheduleResponse> updateSchedule(
            WeekdaysScheduleRequest request,
            Long cpId,
            Long WsId
    ) {

        CompanyEntity company = companyRepository.findById(cpId)
                .orElseThrow(EntityNotFoundException::new);
        WeekdaysScheduleEntity weekdaysSchedule = weekdaysScheduleRepository.findByWsId(WsId)
                .orElseThrow(EntityNotFoundException::new);

        //  빈 요청이면 기본값으로 채우기
        List<WeekdaysScheduleRequest.DayScheduleDto> dtoList =
                resolveScheduleList(request.getScheduleList());

        // 기존 스케줄 전부 삭제
        weekdaysScheduleRepository.deleteByWsId(WsId);



        //  DTO → Entity 변환 & 저장
        List<WeekdaysScheduleEntity> toSave = weekdaysScheduleConvertor
                .toEntity(
                        WeekdaysScheduleRequest.builder().scheduleList(dtoList).build(),
                        company
                );
        List<WeekdaysScheduleEntity> saved = weekdaysScheduleRepository.saveAll(toSave);

        //  저장된 엔티티를 Response DTO 로 변환
        return weekdaysScheduleConvertor.toResponseList(saved);
    }

}
