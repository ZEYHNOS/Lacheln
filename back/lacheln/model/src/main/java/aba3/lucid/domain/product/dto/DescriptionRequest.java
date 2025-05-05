package aba3.lucid.domain.product.dto;

import aba3.lucid.common.enums.DescriptionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DescriptionRequest {

    private DescriptionType type;

    private String value;

}
