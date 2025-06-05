package aba3.lucid.common.api;

import aba3.lucid.common.annotation.Converter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Converter
public class PaginationConverter {

    public Pagination createPagination(Page<?> page, String order) {
        return Pagination.builder()
                .curPage(page.getNumber())
                .curElement(page.getNumberOfElements())
                .size(page.getSize())
                .totalPage(page.getTotalPages())
                .totalElement(page.getTotalElements())
                .order(order)
                .build()
                ;
    }

}
