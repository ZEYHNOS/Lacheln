package aba3.lucid.domain.calendar.dto;

import aba3.lucid.common.enums.Color;
import aba3.lucid.domain.payment.entity.PayDetailEntity;
import aba3.lucid.domain.product.dto.option.OptionDto;
import aba3.lucid.domain.user.entity.UsersEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CalendarDto {

    private Long calDetailId;

    private String title;

    private Color color;

    private LocalTime time;

}
