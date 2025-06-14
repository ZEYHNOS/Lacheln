package aba3.lucid.domain.product.dto;

import aba3.lucid.domain.company.enums.CompanyCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PopularResponse {

    private Long productId;

    private String productName;

    private Long companyId;

    private String companyName;

    private CompanyCategory category;

    private String productImageUrl;

    private Integer rank;

}
