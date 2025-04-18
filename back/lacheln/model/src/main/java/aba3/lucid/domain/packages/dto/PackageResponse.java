package aba3.lucid.domain.packages.dto;

import aba3.lucid.domain.company.dto.CompanyResponse;
import aba3.lucid.domain.product.enums.PackageStatus;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class PackageResponse {

    private long id;

    private String name;

    private String comment;

    private CompanyResponse admin;

    private CompanyResponse cp1;

    private CompanyResponse cp2;

    private LocalDateTime createAt;

    private LocalDateTime endDate;

    private PackageStatus status;

    private int discountrate;

    private String imageUrl;

}
