package aba3.lucid.domain.packages.converter;

import aba3.lucid.common.annotation.Converter;
import aba3.lucid.domain.packages.dto.PackageRequest;
import aba3.lucid.domain.packages.dto.PackageResponse;
import aba3.lucid.domain.packages.entity.PackageEntity;
import aba3.lucid.domain.product.enums.PackageStatus;

import java.time.LocalDateTime;
import java.util.List;

@Converter
public class PackageConverter {


    public PackageResponse toResponse(PackageEntity packageEntity) {
        return PackageResponse.builder()
                .name(packageEntity.getPackName())
                .comment(packageEntity.getPackComment())
                .startDate(packageEntity.getPackStartdate())
                .endDate(packageEntity.getPackEndDate())
                .discountrate(packageEntity.getPackDiscountrate())
                .imageUrl(packageEntity.getPackImageUrl())
                .build()
                ;
    }

    public PackageEntity toEntity(PackageRequest request) {
        return PackageEntity.builder()
                .packName(request.getName())
                .packComment(request.getComment())
                .packDiscountrate(request.getDiscountrate())
                .packStartdate(request.getStartDateOrDefaultDate())
                .packEndDate(request.getEndDateOrDefaultDate())
                .packImageUrl(request.getImageUrlOrDefaultUrl())
                .packStatus(request.getStatusOrDefaultStatus())
                .build()
                ;
    }

    public List<PackageResponse> toResponseList(List<PackageEntity> packageList) {
        return packageList.stream()
                .map(this::toResponse)
                .toList()
                ;
    }
}
