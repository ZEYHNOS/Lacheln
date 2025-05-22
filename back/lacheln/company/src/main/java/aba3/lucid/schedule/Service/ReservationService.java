package aba3.lucid.schedule.Service;

import aba3.lucid.common.enums.Weekdays;
import aba3.lucid.domain.schedule.entity.RegularHolidayEntity;
import aba3.lucid.domain.schedule.entity.TemporaryHolidayEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReservationService {

    private final RegularHolidayService regularHolidayService; // 정기 휴일 service
    private final TemporaryHolidayService temporaryHolidayService; // 임시 휴일 service

    // 해당 월 휴일 리스트
    public List<LocalDate> getHolidayList(Long companyId) {
        Set<LocalDate> holidaySet = new HashSet<>();

        // 임시 휴일 넣기
        holidaySet.addAll(getTemporaryHolidayList(companyId));
        // 정기 휴일 넣기
        holidaySet.addAll(getRegularHolidayList(companyId));

        return holidaySet.stream().toList();
    }



    // 임시 휴일 리스트
    private List<LocalDate> getTemporaryHolidayList(Long companyId) {
        return temporaryHolidayService.findAllByCompanyId(companyId).stream()
                .map(TemporaryHolidayEntity::getThDate)
                .toList()
                ;
    }

    private List<LocalDate> getRegularHolidayList(Long companyId) {
        List<RegularHolidayEntity> regularHolidayEntityList = regularHolidayService.findAllByCompanyId(companyId);
        List<LocalDate> result = new ArrayList<>();

        // 현재 연도와 월
        LocalDate now = LocalDate.now();
        int year = now.getYear();
        int month = now.getMonthValue();

        for (RegularHolidayEntity holiday : regularHolidayEntityList) {

            // 월 조건이 지정되어 있고 현재 월이 아니면 skip
            if (holiday.getRhHdMonth() != 0 && holiday.getRhHdMonth() != month) {
                continue;
            }

            // 일 단위 지정
            if (holiday.getRhHdDays() > 0) {
                try {
                    result.add(LocalDate.of(year, month, holiday.getRhHdDays()));
                } catch (Exception e) {
                    log.warn("Invalid regular holiday day for companyId {}: {}", companyId, e.getMessage());
                }
            }

            // 요일 + 주차 지정
            if (holiday.getRhHdWeekdays() != null && holiday.getRhHdWeek() != null) {
                List<LocalDate> weekdayDates = getDatesForWeekdayInMonth(year, month, holiday.getRhHdWeekdays());

                switch (holiday.getRhHdWeek()) {
                    case ONE -> addIfExists(result, weekdayDates, 0);
                    case TWO -> addIfExists(result, weekdayDates, 1);
                    case THREE -> addIfExists(result, weekdayDates, 2);
                    case FOUR -> addIfExists(result, weekdayDates, 3);
                    case FIVE -> addIfExists(result, weekdayDates, 4);
                    case ODD -> {
                        for (int i = 0; i < weekdayDates.size(); i += 2) {
                            result.add(weekdayDates.get(i));
                        }
                    }
                    case EVEN -> {
                        for (int i = 1; i < weekdayDates.size(); i += 2) {
                            result.add(weekdayDates.get(i));
                        }
                    }
                }
            }
        }

        return result;
    }

    // 해당 월의 특정 요일에 해당하는 날짜 리스트를 반환
    private List<LocalDate> getDatesForWeekdayInMonth(int year, int month, Weekdays weekday) {
        List<LocalDate> dates = new ArrayList<>();
        LocalDate date = LocalDate.of(year, month, 1);
        DayOfWeek targetDay = DayOfWeek.of(weekday.getValue()); // MONDAY = 1, SUNDAY = 7

        while (date.getMonthValue() == month) {
            if (date.getDayOfWeek() == targetDay) {
                dates.add(date);
            }
            date = date.plusDays(1);
        }

        return dates;
    }

    // index 범위 체크 후 추가
    private void addIfExists(List<LocalDate> list, List<LocalDate> source, int index) {
        if (source.size() > index) {
            list.add(source.get(index));
        }
    }
}
