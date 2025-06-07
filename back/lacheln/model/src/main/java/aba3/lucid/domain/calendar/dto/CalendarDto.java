package aba3.lucid.domain.calendar.dto;

import aba3.lucid.domain.payment.entity.PayDetailEntity;
import aba3.lucid.domain.product.dto.option.OptionDto;
import aba3.lucid.domain.user.entity.UsersEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CalendarDto {

    private String title;

    private String content;

    private Long payDetailId;

    private Long cpId;

    private LocalDateTime start;

    private LocalDateTime end;

    private String memo;

    private String productName;

    private String userName;

    private String phoneNum;

    private List<OptionDto> optionDtoList;

    private String managerName;

}
