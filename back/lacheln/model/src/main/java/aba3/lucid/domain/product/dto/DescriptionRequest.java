package aba3.lucid.domain.product.dto;

import aba3.lucid.common.enums.DescriptionType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DescriptionRequest {

    @NotNull
    private DescriptionType type;

    private String value;

}
