package aba3.lucid.schedule.Service;

import aba3.lucid.common.enums.HolidayWeek;
import aba3.lucid.common.enums.Weekdays;
import aba3.lucid.common.exception.ApiException;
import aba3.lucid.common.status_code.ErrorCode;
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

    // 특정 날 일정 리스트
    public List<LocalDateTime> getScheduleDateTime(LocalDate date, Long companyId) {
        // 1. 휴일 체크
        if (isRegularHoliday(date, companyId)) {
            throw new ApiException(ErrorCode.BAD_REQUEST, "정기 휴일입니다.");
        }

        if (temporaryHolidayService.existsByCompanyIdAndDate(companyId, date)) {
            throw new ApiException(ErrorCode.BAD_REQUEST, "임시 휴일입니다.");
        }

        // 2. 요일 추출
        int dayOfWeek = date.getDayOfWeek().getValue(); // 1=월, 7=일
        Weekdays weekday = Weekdays.values()[dayOfWeek - 1]; // Weekdays Enum 매칭

        // 3. 요일별 영업시간 가져오기
        WeekdaysScheduleEntity schedule = weekdaysScheduleService.findByCompanyIdAndWsWeekdays(companyId, weekday);

        // 4. 시작/종료 시간을 LocalDateTime으로 변환
        LocalDateTime startDateTime = LocalDateTime.of(date, schedule.getWsStart());
        LocalDateTime endDateTime = LocalDateTime.of(date, schedule.getWsEnd());

        List<LocalDateTime> result = new ArrayList<>();

        while (!startDateTime.isAfter(endDateTime)) {
            result.add(startDateTime);
            startDateTime = startDateTime.plusMinutes(30);
        }


        return result;
    }


    private List<LocalDate> getScheduleDateList(YearMonth yearMonth,
                                                    List<WeekdaysScheduleEntity> weekdaysScheduleEntityList,
                                                    List<RegularHolidayEntity> regularHolidayEntityList,
                                                    List<TemporaryHolidayEntity> temporaryHolidayEntityList) {
        List<LocalDate> dateList = getDateList(yearMonth);

        Set<LocalDate> holidaySet = initHolidaySet(yearMonth, regularHolidayEntityList, temporaryHolidayEntityList);



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

    /**
     * 특정 연월(YearMonth)에 해당하는 모든 휴일(LocalDate)들을 계산하여 반환합니다.
     * 임시 휴일(TemporaryHolidayEntity)과 정기 휴일(RegularHolidayEntity)을 모두 포함합니다.
     *
     * @param yearMonth 검사 대상 연월
     * @param regularHolidayEntityList 정기 휴일 목록 (요일 + 주차 기준)
     * @param temporaryHolidayEntityList 임시 휴일 목록 (특정 날짜 지정)
     * @return 해당 연월의 모든 휴일을 포함한 Set
     */
    private Set<LocalDate> initHolidaySet(YearMonth yearMonth,
                                          List<RegularHolidayEntity> regularHolidayEntityList,
                                          List<TemporaryHolidayEntity> temporaryHolidayEntityList) {
        Set<LocalDate> set = new HashSet<>();

        for (TemporaryHolidayEntity temporaryHoliday : temporaryHolidayEntityList) {
            set.add(temporaryHoliday.getThDate());
        }

        set.addAll(calculateRegularHolidays(yearMonth, regularHolidayEntityList));

        return set;
    }

    /**
     * 특정 연월 기준으로 모든 정기 휴일 날짜를 계산하여 반환합니다.
     *
     * @param yearMonth 대상 연월
     * @param regularHolidayEntityList 정기 휴일 설정 목록
     * @return 정기 휴일 날짜 Set
     */
    private Set<LocalDate> calculateRegularHolidays(YearMonth yearMonth, List<RegularHolidayEntity> regularHolidayEntityList) {
        Set<LocalDate> set = new HashSet<>();
        for (RegularHolidayEntity regularHoliday : regularHolidayEntityList) {
            Weekdays weekday = regularHoliday.getRhHdWeekdays();
            HolidayWeek week = regularHoliday.getRhHdWeek();

            if (regularHoliday.getRhHdMonth() != 0 &&
                    regularHoliday.getRhHdMonth() != yearMonth.getMonthValue()) {
                continue;
            }

            List<LocalDate> weekdayDates = getDatesOfSpecificWeekdayInMonth(yearMonth, weekday);

            switch (week) {
                case ONE:  addIfValid(set, weekdayDates, 0); break;
                case TWO:  addIfValid(set, weekdayDates, 1); break;
                case THREE:addIfValid(set, weekdayDates, 2); break;
                case FOUR: addIfValid(set, weekdayDates, 3); break;
                case FIVE: addIfValid(set, weekdayDates, 4); break;
                case ODD:
                    for (int i = 0; i < weekdayDates.size(); i += 2) {
                        set.add(weekdayDates.get(i));
                    }
                    break;
                case EVEN:
                    for (int i = 1; i < weekdayDates.size(); i += 2) {
                        set.add(weekdayDates.get(i));
                    }
                    break;
            }
        }
        return set;
    }

    public boolean isRegularHoliday(LocalDate date, Long companyId) {
        YearMonth ym = YearMonth.from(date);
        List<RegularHolidayEntity> regularHolidays = regularHolidayService.findAllByCompanyId(companyId);
        Set<LocalDate> regularHolidayDates = calculateRegularHolidays(ym, regularHolidays);
        return regularHolidayDates.contains(date);
    }



    /**
     * 해당 연월 내에서 특정 요일에 해당하는 모든 날짜들을 반환합니다.
     * 예: 2025년 5월의 모든 화요일(LocalDate)을 반환
     *
     * @param yearMonth 대상 연월
     * @param weekday 찾고자 하는 요일 (예: MONDAY)
     * @return 해당 요일에 해당하는 날짜 리스트
     */
    private List<LocalDate> getDatesOfSpecificWeekdayInMonth(YearMonth yearMonth, Weekdays weekday) {
        List<LocalDate> result = new ArrayList<>();
        for (int day = 1; day <= yearMonth.lengthOfMonth(); day++) {
            LocalDate date = LocalDate.of(yearMonth.getYear(), yearMonth.getMonth(), day);
            if (date.getDayOfWeek().getValue() == weekday.getValue()) {
                result.add(date);
            }
        }
        return result;
    }

    /**
     * 주어진 요일 날짜 리스트에서 인덱스 위치의 날짜를 Set에 추가합니다.
     * 예: "3번째 화요일"이라면 인덱스 2의 값을 추가
     *
     * @param set 추가 대상 Set
     * @param dates 요일 날짜 리스트 (예: 모든 수요일)
     * @param index 추가하려는 날짜 인덱스 (0부터 시작)
     */
    private void addIfValid(Set<LocalDate> set, List<LocalDate> dates, int index) {
        if (index < dates.size()) {
            set.add(dates.get(index));
        }
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
