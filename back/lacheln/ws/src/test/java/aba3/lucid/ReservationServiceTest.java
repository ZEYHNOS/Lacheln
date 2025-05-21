package aba3.lucid;

import aba3.lucid.common.enums.HolidayWeek;
import aba3.lucid.common.enums.Weekdays;
import aba3.lucid.domain.schedule.entity.RegularHolidayEntity;
import aba3.lucid.domain.schedule.entity.TemporaryHolidayEntity;
import aba3.lucid.schedule.Service.RegularHolidayService;
import aba3.lucid.schedule.Service.ReservationService;
import aba3.lucid.schedule.Service.TemporaryHolidayService;
import aba3.lucid.schedule.Service.WeekdaysScheduleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class ReservationServiceTest {

    private RegularHolidayService regularHolidayService;
    private TemporaryHolidayService temporaryHolidayService;
    private WeekdaysScheduleService weekdaysScheduleService;
    private ReservationService reservationService;

    @BeforeEach
    void setUp() {
        regularHolidayService = mock(RegularHolidayService.class);
        temporaryHolidayService = mock(TemporaryHolidayService.class);
        weekdaysScheduleService = mock(WeekdaysScheduleService.class);
        reservationService = new ReservationService(regularHolidayService, temporaryHolidayService, weekdaysScheduleService);
    }

    @Test
    void holidayListTest1() {
        TemporaryHolidayEntity temporaryHoliday = new TemporaryHolidayEntity();
        temporaryHoliday.setThDate(LocalDate.of(2025, 5, 21));
        when(temporaryHolidayService.findAllByCompanyId(1L)).thenReturn(List.of(temporaryHoliday));

        RegularHolidayEntity regularHoliday = new RegularHolidayEntity();
        regularHoliday.setRhHdWeekdays(Weekdays.SUNDAY);
        regularHoliday.setRhHdWeek(HolidayWeek.TWO);
        when(regularHolidayService.findAllByCompanyId(1L)).thenReturn(List.of(regularHoliday));

        List<LocalDate> holidayList = reservationService.getHolidayList(1L);

        assertThat(holidayList).contains(LocalDate.of(2025, 5, 21));
        assertThat(holidayList).contains(LocalDate.of(2025, 5, 11));
    }

    @Test
    void holidayListTest2() {
        RegularHolidayEntity regularHoliday = new RegularHolidayEntity();
        regularHoliday.setRhHdWeekdays(Weekdays.SATURDAY);
        regularHoliday.setRhHdWeek(HolidayWeek.ODD);
        when(regularHolidayService.findAllByCompanyId(1L)).thenReturn(List.of(regularHoliday));

        List<LocalDate> holidayList = reservationService.getHolidayList(1L);

        assertThat(holidayList).contains(LocalDate.of(2025, 5, 11));
    }

}
