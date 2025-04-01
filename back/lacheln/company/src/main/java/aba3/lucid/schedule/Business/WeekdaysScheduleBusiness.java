package aba3.lucid.schedule.Business;


import aba3.lucid.common.exception.ApiException;
import aba3.lucid.common.status_code.ErrorCode;
import aba3.lucid.domain.company.entity.CompanyEntity;
import aba3.lucid.domain.company.repository.CompanyRepository;
import aba3.lucid.domain.schedule.convertor.WeekdaysScheduleConvertor;
import aba3.lucid.domain.schedule.dto.WeekdaysScheduleRequest;
import aba3.lucid.domain.schedule.dto.WeekdaysScheduleResponse;
import aba3.lucid.domain.schedule.entity.WeekdaysScheduleEntity;
import aba3.lucid.domain.schedule.repository.TemporaryHolidayRepository;
import aba3.lucid.domain.schedule.repository.WeekdaysScheduleRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class WeekdaysScheduleBusiness {
    private final WeekdaysScheduleRepository weekdaysScheduleRepository;
    private final TemporaryHolidayRepository temporaryHolidayRepository;
    private final WeekdaysScheduleConvertor weekdaysScheduleConvertor;
    private final CompanyRepository companyRepository;

    public WeekdaysScheduleBusiness(WeekdaysScheduleConvertor weekdaysScheduleConvertor,
                                    WeekdaysScheduleRepository weekdaysScheduleRepository,
                                    TemporaryHolidayRepository temporaryHolidayRepository,
                                    CompanyRepository companyRepository) {
        this.weekdaysScheduleRepository = weekdaysScheduleRepository;
        this.weekdaysScheduleConvertor = weekdaysScheduleConvertor;
        this.temporaryHolidayRepository = temporaryHolidayRepository;
        this.companyRepository = companyRepository;
    }
    public List<WeekdaysScheduleResponse> saveWeekdaysSchedule(WeekdaysScheduleRequest request, long cpId) {
        if(request == null) {
            throw  new ApiException(ErrorCode.BAD_REQUEST, "WeekdaysRequest 값을 못 받았습니다.");
        }

        CompanyEntity company = companyRepository.findById(cpId)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, "업체를 찾을 수 없습니다"));

        List<WeekdaysScheduleEntity> weekdaysScheduleEntities = weekdaysScheduleConvertor.toEntity(request, company);

        List<WeekdaysScheduleEntity> savedEntities = weekdaysScheduleRepository.saveAll(weekdaysScheduleEntities);

        return savedEntities.stream()
                .map(weekdaysScheduleConvertor:: toResponse)
                .collect(Collectors.toList());

    }



}
