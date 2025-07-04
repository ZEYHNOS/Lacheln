package aba3.lucid.product.service;

import aba3.lucid.domain.product.converter.HashtagConverter;
import aba3.lucid.domain.product.converter.OptionConverter;
import aba3.lucid.domain.product.converter.ProductDescriptionConverter;
import aba3.lucid.domain.product.converter.ProductImageConverter;
import aba3.lucid.domain.product.dress.convertor.DressSizeConverter;
import aba3.lucid.domain.product.dress.dto.DressRequest;
import aba3.lucid.domain.product.dress.entity.DressEntity;
import aba3.lucid.domain.product.dress.entity.DressSizeEntity;
import aba3.lucid.domain.product.dress.repository.DressRepository;
import aba3.lucid.domain.product.enums.ProductStatus;
import aba3.lucid.image.service.ImageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class DressService extends ProductAbstractService<DressEntity, DressRequest> {

    private final DressRepository dressRepository;
    private final DressSizeConverter dressSizeConverter;

    public DressService(DressRepository dressRepository,
                        OptionConverter optionConverter,
                        HashtagConverter hashtagConverter,
                        ProductImageConverter productImageConverter,
                        DressSizeConverter dressSizeConverter,
                        ProductDescriptionConverter descriptionConverter,
                        ImageService imageService) {
        super(dressRepository, optionConverter, hashtagConverter, productImageConverter, descriptionConverter, imageService);
        this.dressRepository = dressRepository;
        this.dressSizeConverter = dressSizeConverter;
    }

    @Override
    public List<DressEntity> getActiveProductList(Long companyId) {
        return dressRepository.findAllByCompany_CpIdAndPdStatus(companyId, ProductStatus.ACTIVE);
    }

    @Override
    public List<DressEntity> getValidProductList(Long companyId) {
        return dressRepository.findAllByCompany_CpIdAndPdStatusNot(companyId, ProductStatus.REMOVE);
    }

    @Override
    protected void updateAdditionalFields(DressEntity existingEntity, DressRequest request) {
        List<DressSizeEntity> dressSizeEntityList = dressSizeConverter.toEntityList(request.getSizeList(), existingEntity);
        existingEntity.setAdditionalField(request, dressSizeEntityList);
    }
}

