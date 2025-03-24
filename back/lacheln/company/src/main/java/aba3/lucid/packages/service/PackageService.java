package aba3.lucid.packages.service;

import aba3.lucid.common.exception.ApiException;
import aba3.lucid.common.status_code.ErrorCode;
import aba3.lucid.domain.packages.dto.PackageUpdateRequest;
import aba3.lucid.domain.packages.entity.PackageEntity;
import aba3.lucid.domain.product.repository.PackageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class PackageService {

    private final PackageRepository packageRepository;

    // 패키지 등록
    public PackageEntity packageRegister(PackageEntity packageEntity) {
        return packageRepository.save(packageEntity);
    }

    // 패키지 정보 수정
    public PackageEntity packageUpdate(PackageEntity packageEntity, PackageUpdateRequest request) {
        // TODO 상태 변경 로직
        packageEntity.updateAdditionalField(request);

        return packageRepository.save(packageEntity);
    }


    public PackageEntity findByIdWithThrow(long packageId) {
        return packageRepository.findById(packageId)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND));
    }
}
