package aba3.lucid.domain.packages.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigInteger;

@Data
@Builder
public class PackageProductResponse {

    private String productName;

    private String companyName;

    private BigInteger price;

    private String imageUrl;

}
