package aba3.lucid.domain.company.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;


@Getter
@Builder
public class CompanyAllResponse <T>{
    private List<T> companies;
    private int page;
    private int size;
    private long totalElements;
    private int totalPages;
    private boolean last;
}
