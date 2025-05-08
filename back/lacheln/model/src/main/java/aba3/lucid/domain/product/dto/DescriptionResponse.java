package aba3.lucid.domain.product.dto;

import aba3.lucid.common.enums.DescriptionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DescriptionResponse {

    private DescriptionType type;

    private String value;

    private int order;

}
