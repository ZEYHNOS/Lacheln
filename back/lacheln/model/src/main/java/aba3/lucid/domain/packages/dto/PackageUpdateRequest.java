package aba3.lucid.domain.packages.dto;

import aba3.lucid.domain.product.dto.DescriptionRequest;
import aba3.lucid.domain.product.enums.PackageStatus;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PackageUpdateRequest {

    private String name;

    private List<DescriptionRequest> descriptionRequestList;

    private PackageStatus status;

    private int discountrate;

    private LocalDateTime endDate;

    private String imageUrl;


}
