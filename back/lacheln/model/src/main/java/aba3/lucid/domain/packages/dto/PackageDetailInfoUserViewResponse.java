package aba3.lucid.domain.packages.dto;

import aba3.lucid.domain.product.dto.DescriptionResponse;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PackageDetailInfoUserViewResponse {

    private Long packageId;

    private String name;

    private List<DescriptionResponse> descriptionList;

    private String imageUrl;

    private List<PackageProductResponse> productInfoList;

    private int discountrate;
}
