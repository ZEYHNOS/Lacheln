package aba3.lucid.product.service;

import aba3.lucid.domain.company.enums.CompanyCategory;
import aba3.lucid.domain.packages.dto.PackageDetailInfoUserViewResponse;
import aba3.lucid.domain.packages.dto.PackageProductResponse;
import aba3.lucid.image.service.ImageService;
import aba3.lucid.domain.product.converter.ProductDescriptionConverter;
import aba3.lucid.domain.product.converter.HashtagConverter;
import aba3.lucid.domain.product.converter.OptionConverter;
import aba3.lucid.domain.product.converter.ProductImageConverter;
import aba3.lucid.domain.product.enums.ProductStatus;
import aba3.lucid.domain.product.makeup.dto.MakeupRequest;
import aba3.lucid.domain.product.makeup.entity.MakeupEntity;
import aba3.lucid.domain.product.makeup.repository.MakeupRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class MakeupService extends ProductAbstractService<MakeupEntity, MakeupRequest> {

    private final MakeupRepository makeupRepository;

    public MakeupService(MakeupRepository makeupRepository,
                         OptionConverter optionConverter,
                         HashtagConverter hashtagConverter,
                         ProductImageConverter imageConverter,
                         ProductDescriptionConverter descriptionConverter,
                         ImageService imageService) {
        super(makeupRepository, optionConverter, hashtagConverter, imageConverter, descriptionConverter, imageService);
        this.makeupRepository = makeupRepository;
    }

    @Override
    public List<MakeupEntity> getActiveProductList(Long companyId) {
        return makeupRepository.findAllByCompany_CpIdAndPdStatus(companyId, ProductStatus.ACTIVE);
    }

    @Override
    public List<MakeupEntity> getValidProductList(Long companyId) {
        return makeupRepository.findAllByCompany_CpIdAndPdStatusNot(companyId, ProductStatus.REMOVE);
    }

    @Override
    protected void updateAdditionalFields(MakeupEntity existingEntity, MakeupRequest request) {
        existingEntity.setAdditionalField(request);
    }

    public void injectManagerInfo(PackageDetailInfoUserViewResponse response) {
        for (PackageProductResponse packageProduct : response.getProductInfoList()) {
            if (packageProduct.getCategory().equals(CompanyCategory.M)) {
                MakeupEntity entity = findByIdWithThrow(packageProduct.getPdId());
                packageProduct.setManager(entity.getMakeupManager());
                break;
            }
        }
    }
}

