package aba3.lucid.domain.packages.dto;

import aba3.lucid.domain.company.enums.CompanyCategory;
import aba3.lucid.domain.product.dto.option.OptionDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PackageCompanyResponse {

    private Long id;

    private String email;

    private String name;

    private String address;

    private CompanyCategory category;

    private String profileImageUrl;

    private Long productId;

    private String productName;

    private BigInteger productPrice;

    List<OptionDto> optionDtoList;

}
