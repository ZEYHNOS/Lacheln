package aba3.lucid.domain.packages.dto;

import aba3.lucid.common.annotation.ValidList;
import aba3.lucid.domain.product.dto.DescriptionRequest;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PackageUpdateRequest {

    @NotBlank
    @Size(min = 3, max = 50)
    private String name;

    @Valid
    @ValidList
    private List<DescriptionRequest> descriptionRequestList;

    @Min(0)
    @Max(95)
    private int discountrate;

    @Future
    private LocalDateTime endDate;

    @NotBlank
    private String imageUrl;

    private List<String> hashTagList;

    @NotNull
    private LocalTime taskTime;


}
