package aba3.lucid.domain.packages.dto;

import aba3.lucid.domain.product.dto.DescriptionRequest;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
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

    private String name;

    private List<DescriptionRequest> descriptionRequestList;

    private int discountrate;

    private LocalDateTime endDate;

    private String imageUrl;

    private List<String> hashTagList;

    private LocalTime taskTime;


}
