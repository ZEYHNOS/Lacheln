package aba3.lucid.common.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Pagination {

    // 현재 페이지
    private Integer curPage;

    // 현재 페이지에 있는 요소 수
    private Integer curElement;

    // 페이지의 원소 개수 설정
    private Integer size;

    // 총 페이지
    private Integer totalPage;

    // 총 요소 수
    private Long totalElement;

    // 정렬 기준 (word, added_date)
    private String sortBy;

    // 정렬 순서 (asc, desc)
    private String order;

}
