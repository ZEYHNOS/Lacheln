package aba3.lucid.domain.user.dto;

import aba3.lucid.common.enums.ActiveEnum;
import aba3.lucid.domain.user.enums.GenderEnum;
import aba3.lucid.domain.user.enums.TierEnum;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
public class UserAllResponse<T> {

    private List<T> users;
    private int page;
    private int size;
    private long totalElements;
    private int totalPages;
    private boolean last;

}
