package aba3.lucid.Calendar.Business;


import aba3.lucid.Calendar.Service.CalendarService;
import aba3.lucid.common.annotation.Business;
import aba3.lucid.common.exception.ApiException;
import aba3.lucid.common.status_code.ErrorCode;
import aba3.lucid.common.validate.Validator;
import aba3.lucid.domain.calendar.convertor.CalendarConvertor;
import aba3.lucid.domain.calendar.convertor.CalendarUpdateConvertor;
import aba3.lucid.domain.calendar.dto.CalendarRequest;
import aba3.lucid.domain.calendar.dto.CalendarResponse;
import aba3.lucid.domain.calendar.entity.CalendarEntity;
import aba3.lucid.domain.company.entity.CompanyEntity;
import aba3.lucid.domain.company.repository.CompanyRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Business
@RequiredArgsConstructor
public class CalendarBusiness {

    private final CalendarService calendarService;
    private final CalendarConvertor calendarConvertor;
    private final CompanyRepository companyRepository;
    private final CalendarUpdateConvertor calendarUpdateConvertor;

    public CalendarResponse createCalendar(CalendarRequest request, Long cpId) {
        CompanyEntity company = companyRepository.findById(cpId).orElseThrow(EntityNotFoundException::new);

        if (company == null) {
            throw new EntityNotFoundException("Company not found with id " + cpId);
        }
        CalendarEntity calendarEntity = calendarConvertor.toEntity(request,company);

        CalendarEntity savedEntity = calendarService.createCalendar(calendarEntity);
        return calendarConvertor.toResponse(savedEntity);

    }

    public CalendarResponse updateCalendar(CalendarRequest request, Long cpId, Long calId) {
        CompanyEntity newCompany = companyRepository.findById(cpId).orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND,
                "업체를 찾을 수 없습니다" + cpId));
        CalendarEntity updatedCalendar = calendarUpdateConvertor.toEntity(request,newCompany,calId);
        CalendarEntity updatedEntity = calendarService.updateCalendar(updatedCalendar, calId);

        return calendarConvertor.toResponse(updatedEntity);

    }


}
