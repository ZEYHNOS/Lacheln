package aba3.lucid.schedule.Business;

import aba3.lucid.common.annotation.Business;
import aba3.lucid.common.auth.CustomUserDetails;
import aba3.lucid.common.validate.Validator;
import aba3.lucid.company.service.CompanyService;
import aba3.lucid.domain.company.entity.CompanyEntity;
import aba3.lucid.domain.schedule.convertor.WeekdaysScheduleConvertor;
import aba3.lucid.domain.schedule.dto.BusinessHourResponse;
import aba3.lucid.domain.schedule.dto.WeekdaysScheduleRequest;
import aba3.lucid.domain.schedule.dto.WeekdaysScheduleResponse;
import aba3.lucid.domain.schedule.entity.WeekdaysScheduleEntity;
import aba3.lucid.schedule.Service.WeekdaysScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Business
@RequiredArgsConstructor
@Component
public class WeekdaysScheduleBusiness {

    private final WeekdaysScheduleService weekdaysScheduleService;
    private final CompanyService companyService;
    private final WeekdaysScheduleConvertor weekdaysScheduleConvertor;

    public WeekdaysScheduleResponse createSchedule(WeekdaysScheduleRequest request, CustomUserDetails customUserDetails) {
        Validator.throwIfNull(request);
        Long cpId = customUserDetails.getCompanyId();
        CompanyEntity company = companyService.findByIdWithThrow(cpId);
        WeekdaysScheduleEntity entity = weekdaysScheduleConvertor.toEntity(request,company);
        entity.setCompany(company);
        WeekdaysScheduleEntity savedEntity = weekdaysScheduleService.createWeekdaysSchedule(entity);
        return weekdaysScheduleConvertor.toResponse(savedEntity);

    }

    public WeekdaysScheduleResponse updateSchedule(WeekdaysScheduleRequest request, Long wsId) {
        Validator.throwIfNull(request);
        Validator.throwIfInvalidId(wsId);
        weekdaysScheduleService.findByThrowId(wsId);
        WeekdaysScheduleEntity updateEntity = weekdaysScheduleService.updateWeekdaysSchedule(wsId, request);
        return weekdaysScheduleConvertor.toResponse(updateEntity);

    }

//    public List<WeekdaysScheduleRequest> getDefaultSchedules() {
//        List<WeekdaysScheduleRequest> defaults = new ArrayList<>();
//        String[] days = {"Mon", "Tue", "Wed", "Thu", "Fri"};
//        for (String d : days) {
//            defaults.add(
//                    WeekdaysScheduleRequest.builder()
//                            .weekday(Weekdays.valueOf(d))
//                            .start(LocalTime.of(9, 0))
//                            .end(LocalTime.of(18, 0))
//                            .build()
//            );
//        }
//        defaults.add(
//                WeekdaysScheduleRequest.builder()
//                        .weekday(Weekdays.SATURDAY)
//                        .start(LocalTime.of(10, 0))
//                        .end(LocalTime.of(20, 0))
//                        .build()
//        );
//        defaults.add(
//                WeekdaysScheduleRequest.builder()
//                        .weekday(Weekdays.SUNDAY)
//                        .start(null)
//                        .end(null)
//                        .build()
//        );
//        return defaults;
//    }



    //     영업시간
    public BusinessHourResponse[] getBusinessHourArray(Long companyId) {
        return weekdaysScheduleService.getBusinessHour(companyId);
}



}
