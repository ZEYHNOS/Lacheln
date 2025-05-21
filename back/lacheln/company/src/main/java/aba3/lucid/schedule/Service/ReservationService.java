package aba3.lucid.schedule.Service;

import aba3.lucid.common.enums.Weekdays;
import aba3.lucid.common.exception.ApiException;
import aba3.lucid.common.status_code.ErrorCode;
import aba3.lucid.company.service.CompanyService;
import aba3.lucid.domain.company.entity.CompanyEntity;
import aba3.lucid.domain.product.entity.ProductEntity;
import aba3.lucid.domain.schedule.entity.RegularHolidayEntity;
import aba3.lucid.domain.schedule.entity.WeekdaysScheduleEntity;
import aba3.lucid.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReservationService {

    private final RegularHolidayService regularHolidayService; // 정기 휴일 service
    private final TemporaryHolidayService temporaryHolidayService; // 임시 휴일 service
    private final WeekdaysScheduleService weekdaysScheduleService; // 요일별 일정 service 월~일 시간
    private final ProductService productService; // 해당 상품의 갯수 파악을 위한 service
    private final CompanyService companyService;


    /**
     * 예약할 수 있는 날짜 리스트
     * @param date : 예약 하고 싶은 년 / 월
     */
    public List<LocalDateTime> getBookableScheduleList(Long companyId, YearMonth yearMonth) {
        CompanyEntity company = companyService.findByIdWithThrow(companyId);

        // 정기 휴일 + 요일별 일정 정보 가지고 오기
//        List<RegularHolidayEntity> regularHolidayEntityList = regularHolidayService.findByCompanyWithThrow(company);
//        List<WeekdaysScheduleEntity> weekdaysScheduleEntityList = weekdaysScheduleService.findByCompanyWithThrow(company);
        List<RegularHolidayEntity> regularHolidayEntityList = new ArrayList<>(); // TODO Service 로직 작성 시 삭제하기
        List<WeekdaysScheduleEntity> weekdaysScheduleEntityList = new ArrayList<>(); // TODO Service 로직 작성 시 삭제하기



        return List.of();
    }

    // 업체 요일별 일정 배열로 담기
    public WeekdaysScheduleEntity[] getWeekdaysScheduleArray(List<WeekdaysScheduleEntity> weekdaysScheduleEntityList) {
        WeekdaysScheduleEntity[] weekdaysScheduleEntityArray = new WeekdaysScheduleEntity[8];

        for (WeekdaysScheduleEntity entity : weekdaysScheduleEntityList) {
            weekdaysScheduleEntityArray[entity.getWsWeekdays().getValue()] = entity;
        }

        return weekdaysScheduleEntityArray;
    }

    // 해당 년월의 요일 리스트 반환
    public List<LocalDateTime> getMonthOfWeek(YearMonth yearMonth, WeekdaysScheduleEntity[] weekdaysScheduleEntityArray, ProductEntity product) {
        List<LocalDateTime> result = new java.util.ArrayList<>();
        for (int i = 1; i <= yearMonth.lengthOfMonth(); i++) {
            LocalDate date = yearMonth.atDay(i);
            int weekdayIndex = date.getDayOfWeek().getValue(); // 월요일 1, 일요일 7
            WeekdaysScheduleEntity schedule = weekdaysScheduleEntityArray[weekdayIndex];

            if (schedule == null) {
                throw new ApiException(ErrorCode.SERVER_ERROR);
            }

            LocalDateTime start = date.atTime(schedule.getWsStart());
            LocalDateTime end = date.atTime(schedule.getWsEnd());

            while(!start.isAfter(end.minusMinutes(product.getPdTaskTime()))) {
                result.add(start);
                start = start.plusMinutes(30);
            }
        }
        return result;
    }

    // 특정 날짜 예약하기(블락 시켜주기)
    public void reservation() {

    }

}
