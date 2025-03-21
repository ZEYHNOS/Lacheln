package aba3.lucid.domain.packages.converter;

import aba3.lucid.common.annotation.Converter;
import aba3.lucid.common.enums.BinaryChoice;
import aba3.lucid.domain.company.entity.CompanyEntity;
import aba3.lucid.domain.packages.entity.PackageEntity;
import aba3.lucid.domain.packages.entity.PackageToCompanyEntity;

@Converter
public class PackToCpConverter  {

    public PackageToCompanyEntity toEntity(PackageEntity packageEntity, CompanyEntity company) {
        return PackageToCompanyEntity.builder()
                .packageEntity(packageEntity)
                .company(company)
                .packCpMapPass(BinaryChoice.N)
                .build()
                ;
    }
}
