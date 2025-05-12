package aba3.lucid.domain.packages.dto;

import aba3.lucid.domain.company.dto.CompanyResponse;
import aba3.lucid.domain.product.dto.DescriptionResponse;
import aba3.lucid.domain.product.enums.PackageStatus;
import lombok.*;

import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class PackageResponse {

    private Long id;

    private String name;

    private List<DescriptionResponse> descriptionResponseList;

    private CompanyResponse admin;

    private CompanyResponse cp1;

    private CompanyResponse cp2;

    private LocalDateTime createAt;

    private LocalDateTime endDate;

    private PackageStatus status;

    private int discountrate;

    private String imageUrl;

    private BigInteger totalPrice;

}
