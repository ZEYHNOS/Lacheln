package aba3.lucid.domain.packages.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class ProductPackageInsertResponse {

    private String companyName;

    private String packageName;

    private String productName;

}
