package aba3.lucid.domain.packages.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class PackageUserViewListResponse {

    private Long packageId;

    private String packageName;

    private String imageUrl;

    private List<PackageProductResponse> packageProductResponseList;

    private LocalDateTime createAt;

    private int discountrate;

}
