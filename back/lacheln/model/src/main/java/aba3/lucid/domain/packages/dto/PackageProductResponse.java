package aba3.lucid.domain.packages.dto;

import aba3.lucid.domain.company.enums.CompanyCategory;
import aba3.lucid.domain.product.dto.option.OptionDto;
import lombok.Builder;
import lombok.Data;

import java.math.BigInteger;
import java.util.List;

@Data
@Builder
public class PackageProductResponse {

    private Long pdId;

    private Long cpId;

    private String productName;

    private String companyName;

    private String manager;

    private CompanyCategory category;

    private BigInteger price;

    private String imageUrl;

    List<OptionDto> optionDtoList;

}
