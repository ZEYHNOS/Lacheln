package aba3.lucid.domain.packages.dto;

import jakarta.validation.constraints.NegativeOrZero;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PackageGroupCreateRequest {

    @NegativeOrZero
    private long companyId1;

    @NegativeOrZero
    private long companyId2;

    @NegativeOrZero
    private long companyId3;

}
