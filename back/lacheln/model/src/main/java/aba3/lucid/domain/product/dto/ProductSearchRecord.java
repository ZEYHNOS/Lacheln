package aba3.lucid.domain.product.dto;

import aba3.lucid.domain.company.enums.CompanyCategory;
import org.springframework.data.domain.Pageable;

public record ProductSearchRecord(
        String productName,
        String companyName,
        CompanyCategory category,
        Integer minimum,
        Integer maximum,
        String orderBy,
        Boolean isAsc
) {
}
