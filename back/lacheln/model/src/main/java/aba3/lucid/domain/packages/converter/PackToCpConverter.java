package aba3.lucid.domain.packages.converter;

import aba3.lucid.common.annotation.Converter;
import aba3.lucid.domain.packages.dto.PackageGroupCreateRequest;
import aba3.lucid.domain.packages.entity.PackageToCompanyEntity;

@Converter
public class PackToCpConverter  {

    public PackageToCompanyEntity toEntity(PackageGroupCreateRequest request) {
        return PackageToCompanyEntity.builder()
                .build()
                ;
    }

}
