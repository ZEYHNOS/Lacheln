package aba3.lucid.Calendar.Business;


import aba3.lucid.Calendar.Service.CalendarService;
import aba3.lucid.common.annotation.Business;
import aba3.lucid.common.enums.Color;
import aba3.lucid.common.exception.ApiException;
import aba3.lucid.common.status_code.ErrorCode;
import aba3.lucid.domain.calendar.convertor.CalendarConvertor;
import aba3.lucid.domain.calendar.convertor.CalendarUpdateConvertor;
import aba3.lucid.domain.calendar.dto.*;
import aba3.lucid.domain.calendar.entity.CalendarEntity;
import aba3.lucid.domain.calendar.repository.CalendarRepository;
import aba3.lucid.domain.company.entity.CompanyEntity;
import aba3.lucid.domain.company.repository.CompanyRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Business
@RequiredArgsConstructor
public class CalendarBusiness {
    private final CalendarService calendarService;
    private final CalendarConvertor calendarConvertor;
    private final CompanyRepository companyRepository;
    private final CalendarUpdateConvertor calendarUpdateConvertor;
    private final CalendarRepository calendarRepository;

    public CalendarResponse createCalendar(CalendarRequest request, Long cpId) {

        CompanyEntity company = companyRepository.findById(cpId).orElseThrow(EntityNotFoundException::new);
        LocalDate calDate = Optional.ofNullable(request.getDate())
                .orElse(LocalDate.now());

        // details 기본값: 없거나 빈 리스트면, 하나짜리 기본 일정 채워 넣기
        List<CalendarDetailRequest> realDetails = Optional.ofNullable(request.getDetails())
                .filter(dl -> !dl.isEmpty())
                .orElseGet(() -> {
                    CalendarDetailRequest def = CalendarDetailRequest.builder()
                            .title("Basic")
                            .content("Basic1.")
                            .startTime(calDate.atStartOfDay())
                            .endTime  (calDate.atTime(LocalTime.MAX))
                            .color    (Color.BLUE)
                            .memo     ("")
                            .build();
                    return List.of(def);
                });

//        CalendarEntity calendarEntity = calendarConvertor.toEntity(request,company);
        //새 CalendarRequest 인스턴스 생성
        CalendarRequest effectiveRequest = request.toBuilder()
                .date   (calDate)
                .details(realDetails)
                .build();

        // 새 CalendarRequest 인스턴스 생성
        CalendarEntity calendar = calendarConvertor.toEntity(effectiveRequest, company);
        CalendarEntity saved   = calendarService.createCalendar(calendar);

        // Response
        return calendarConvertor.toResponse(saved);

    }


    @Transactional
    public CalendarUpdateResponse updateCalendar(CalendarUpdateRequest request, Long cpId, Long calId) {
        //callId에 해당하는 기존 CalendarEntity를 데이터베이스에 조회함
        CalendarEntity existingEntity = calendarService.findById(calId);

        // // cpId에 해당하는 CompanyEntity를 데이터베이스에서 조회하거나, 없으면 예외를 발생시킵니다
        CompanyEntity company = companyRepository.findById(cpId)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, "업체를 찾을 수 없다." + cpId));

        // 요청(request) 데이터를 바탕으로 기존 엔티티(existingEntity)를 업데이트합니다.
        // CalendarUpdateConvertor의 toEntity 메소드가 기존 엔티티를 수정합니다.
        CalendarEntity updatedCalendar = calendarUpdateConvertor.toEntity(request, company, calId, existingEntity);
        CalendarEntity savedCalendar = calendarRepository.save(updatedCalendar);
        return calendarUpdateConvertor.toResponse(savedCalendar, company, calId);
    }
    @Transactional
    public CalendarEntity updateCalendarEntity(CalendarEntity updatedEntity, Long calId) {
        CalendarEntity existing = calendarRepository.findById(calId)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, "Calendar ID not found: " + calId));
        // 기존 엔티티의 날짜(calDate)를 업데이트합니다.
        existing.setCalDate(updatedEntity.getCalDate());
        existing.setCompany(updatedEntity.getCompany());
        // 기존 엔티티의 CalendarDetailEntity 목록을 업데이트합니다.
        existing.setCalendarDetailEntity(updatedEntity.getCalendarDetailEntity());

        return calendarService.updateCalendar(existing);
    }


}
