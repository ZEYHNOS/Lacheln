package aba3.lucid.domain.product.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ProductSortBy {

    MINIMUM("minimum"),
    CREATE_AT("createAt"),
    NAME("pdName")
    ;

    private final String sortBy;

}
