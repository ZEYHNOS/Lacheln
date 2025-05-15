package aba3.lucid.schedule.Service;

import aba3.lucid.company.service.CompanyService;
import aba3.lucid.domain.company.entity.CompanyEntity;
import aba3.lucid.domain.product.entity.ProductEntity;
import aba3.lucid.domain.schedule.entity.RegularHolidayEntity;
import aba3.lucid.domain.schedule.entity.TemporaryHolidayEntity;
import aba3.lucid.domain.schedule.entity.WeekdaysScheduleEntity;
import aba3.lucid.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReservationService {

    private final RegularHolidayService regularHolidayService; // 정기 휴일 service
    private final ScheduleService scheduleService;  // 일정 service
    private final TemporaryHolidayService temporaryHolidayService; // 임시 휴일 service
    private final WeekdaysScheduleService weekdaysScheduleService; // 요일별 일정 service 월~일 시간
    private final ProductService productService; // 해당 상품의 갯수 파악을 위한 service
    private final CompanyService companyService;


    // 휴일을 제외한 영업 날짜 리스트 반환
    public List<LocalDate> getBookableScheduleList(Long companyId, Long productId, YearMonth yearMonth) {
        CompanyEntity company = companyService.findByIdWithThrow(companyId);
        ProductEntity product = productService.findByIdWithThrow(productId);

        // 해당 업체 월 영업시간 리스트
        List<WeekdaysScheduleEntity> weekdaysScheduleEntityList = weekdaysScheduleService.findAllByCompanyId(companyId);

        // 해당 업체 월 임시휴일 리스트
        List<RegularHolidayEntity> regularHolidayEntityList = regularHolidayService.findAllByCompanyId(companyId);

        // 해당 업체 정기휴일 리스트
        List<TemporaryHolidayEntity> temporaryHolidayEntityList = temporaryHolidayService.findAllByCompanyId(companyId);

        return getScheduleDateList(yearMonth, weekdaysScheduleEntityList, regularHolidayEntityList, temporaryHolidayEntityList);
    }

    // 특정 날의 일정 리스트
    public List<LocalDateTime> getScheduleDateTime(LocalDate date) {
        // 정기 휴일 가지고 오기

        // 임시 휴일 DB에 데이터가 존재하는지 확인하기

        // 업체 요일별 시간 데이터 가지고 오기(요일을 통해서 가지고 오기)


        return null;
    }

    private List<LocalDate> getScheduleDateList(YearMonth yearMonth,
                                                    List<WeekdaysScheduleEntity> weekdaysScheduleEntityList,
                                                    List<RegularHolidayEntity> regularHolidayEntityList,
                                                    List<TemporaryHolidayEntity> temporaryHolidayEntityList) {
        List<LocalDate> dateList = getDateList(yearMonth);

        Set<LocalDate> holidaySet = initHolidaySet(regularHolidayEntityList, temporaryHolidayEntityList);



        return getScheduleList(dateList, holidaySet);
    }

    private List<LocalDate> getScheduleList(List<LocalDate> dateList, Set<LocalDate> holidaySet) {
        List<LocalDate> result = new ArrayList<>();
        for (LocalDate date : dateList) {
            if (!holidaySet.contains(date)) {
                result.add(date);
            }
        }

        return result;
    }

    private Set<LocalDate> initHolidaySet(List<RegularHolidayEntity> regularHolidayEntityList, List<TemporaryHolidayEntity> temporaryHolidayEntityList) {
        Set<LocalDate> set = new HashSet<>();

        return set;
    }

    private List<LocalDate> getDateList(YearMonth yearMonth) {
        List<LocalDate> result = new ArrayList<>();
        for (int day = 1; day <= yearMonth.lengthOfMonth(); day++) {
            result.add(LocalDate.of(yearMonth.getYear(), yearMonth.getMonth(), day));
        }

        return result;
    }

    private WeekdaysScheduleEntity[] getWeekdaysScheduleEntityArr(List<WeekdaysScheduleEntity> weekdaysScheduleEntityList) {

        return null;
    }

    // 특정 날짜 예약하기(블락 시켜주기)
    public void reservation() {

    }

}
