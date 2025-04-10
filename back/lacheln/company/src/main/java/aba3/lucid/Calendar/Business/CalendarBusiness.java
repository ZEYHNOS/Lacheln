package aba3.lucid.Calendar.Business;


import aba3.lucid.Calendar.Service.CalendarService;
import aba3.lucid.common.annotation.Business;
import aba3.lucid.common.validate.Validator;
import aba3.lucid.domain.calendar.convertor.CalendarConvertor;
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

    public CalendarResponse createCalendar(CalendarRequest request, Long cpId) {
        CompanyEntity company = companyRepository.findById(cpId).orElseThrow(EntityNotFoundException::new);

        if (company == null) {
            throw new EntityNotFoundException("Company not found with id " + cpId);
        }
        CalendarEntity calendarEntity = calendarConvertor.toEntity(request,company);

        CalendarEntity savedEntity = calendarService.createCalendar(calendarEntity);
        return calendarConvertor.toResponse(savedEntity);

    }
//    public CalendarResponse updateCalendar(Long calId, CalendarRequest request, Long cpId) {
//        CalendarEntity updatedCalendar = calendarConvertor.toEntity(request,);
//        CalendarEntity updatedEntity = calendarService.updateCalendar(updatedCalendar, calId);
//
//        return calendarConvertor.toResponse(updatedEntity);
//
//    }


}
