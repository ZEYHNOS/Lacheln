package aba3.lucid.packages.service;

import aba3.lucid.domain.packages.entity.PackageEntity;
import aba3.lucid.domain.packages.entity.PackageToCompanyEntity;
import aba3.lucid.domain.product.repository.PackageRepository;
import aba3.lucid.domain.product.repository.PackageToCompanyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class PackageService {

    private final PackageRepository packageRepository;
    private final PackageToCompanyRepository packToCpRepository;

    public PackageToCompanyEntity createPackageGroup(PackageToCompanyEntity entity) {
        // 초대 된 패키지 업체 알림 보내기
        return packToCpRepository.save(entity);
    }

    public PackageEntity packageRegister(PackageEntity entity) {
        return packageRepository.save(entity);
    }
}
