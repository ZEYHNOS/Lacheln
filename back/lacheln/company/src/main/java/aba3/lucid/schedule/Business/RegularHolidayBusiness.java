package aba3.lucid.schedule.Business;


import aba3.lucid.common.annotation.Business;
import aba3.lucid.common.exception.ApiException;
import aba3.lucid.common.status_code.ErrorCode;
import aba3.lucid.domain.company.entity.CompanyEntity;
import aba3.lucid.domain.company.repository.CompanyRepository;
import aba3.lucid.domain.schedule.convertor.RegularHolidayConvertor;
import aba3.lucid.domain.schedule.dto.RegularHolidayRequest;
import aba3.lucid.domain.schedule.dto.RegularHolidayResponse;
import aba3.lucid.domain.schedule.entity.RegularHolidayEntity;
import aba3.lucid.domain.schedule.repository.RegularHolidayRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Business
@RequiredArgsConstructor
@Component
public class RegularHolidayBusiness {
    private final RegularHolidayRepository regularHolidayRepository;
    private final RegularHolidayConvertor regularHolidayConvertor;
    private final CompanyRepository companyRepository;


    public RegularHolidayResponse createHoliday(RegularHolidayRequest request,Long cpId) {
        CompanyEntity company = companyRepository.findById(cpId).orElseThrow(
                () -> new ApiException(ErrorCode.NOT_FOUND,"업체를 찾을 수 없습니다." + cpId));
        String weekdays = (request.getWeekdays() != null) ? request.getWeekdays() : "MON";
        String week = (request.getWeek() != null) ? request.getWeek(): "1";
        int days = (request.getDays() > 0) ? request.getDays() : 1;
        int months = (request.getMonths() > 0) ? request.getMonths() : 1;

        RegularHolidayRequest defaultRequest = new RegularHolidayRequest(weekdays, week, days, months);
        RegularHolidayEntity entity = regularHolidayConvertor.toEntity(defaultRequest, company);
        RegularHolidayEntity savedEntity = regularHolidayRepository.save(entity);
        return regularHolidayConvertor.toResponse(savedEntity);

    }


}
