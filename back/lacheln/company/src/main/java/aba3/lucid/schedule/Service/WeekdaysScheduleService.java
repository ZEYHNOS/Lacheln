package aba3.lucid.schedule.Service;


import aba3.lucid.common.exception.ApiException;
import aba3.lucid.common.status_code.ErrorCode;
import aba3.lucid.domain.company.entity.CompanyEntity;
import aba3.lucid.domain.company.repository.CompanyRepository;
import aba3.lucid.domain.schedule.convertor.WeekdaysScheduleConvertor;
import aba3.lucid.domain.schedule.dto.WeekdaysScheduleRequest;
import aba3.lucid.domain.schedule.dto.WeekdaysScheduleResponse;
import aba3.lucid.domain.schedule.entity.WeekdaysScheduleEntity;
import aba3.lucid.domain.schedule.repository.WeekdaysScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WeekdaysScheduleService {
    private final WeekdaysScheduleRepository weekdaysScheduleRepository;
    private final CompanyRepository companyRepository;
    private final WeekdaysScheduleConvertor convertor;


    @Transactional
    public void saveOrUpdateSchedules(Long cpId, WeekdaysScheduleRequest request) {
        CompanyEntity company = companyRepository.findById(cpId)
                .orElseThrow(()->new ApiException(ErrorCode.NOT_FOUND, "회사를 찾을 수 없습니다."));
        //기존 스케줄 삭제???????????

        //요청 DTO를 엔티티 리스트로 반환
        List<WeekdaysScheduleEntity> newSchedules = convertor.toEntityList(request,company);
//        WeekdaysScheduleRepository.saveAll(newSchedules);

    }

    @Transactional
    public List<WeekdaysScheduleResponse> getSchedules(Long cpId) {
        List<WeekdaysScheduleEntity> entities = weekdaysScheduleRepository.findByCompany_CpId(cpId);
        return convertor.toResponseList(entities);
    }
}
