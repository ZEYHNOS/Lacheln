package aba3.lucid.product.service;

import aba3.lucid.common.validate.Validator;
import aba3.lucid.domain.product.converter.HashtagConverter;
import aba3.lucid.domain.product.converter.OptionConverter;
import aba3.lucid.domain.product.converter.ProductImageConverter;
import aba3.lucid.domain.product.enums.ProductStatus;
import aba3.lucid.domain.product.studio.dto.StudioRequest;
import aba3.lucid.domain.product.studio.entity.StudioEntity;
import aba3.lucid.domain.product.studio.repository.StudioRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class StudioService extends ProductAbstractService<StudioEntity, StudioRequest> {

    private final StudioRepository studioRepository;

    public StudioService(StudioRepository studioRepository
            , OptionConverter optionConverter
            , HashtagConverter hashtagConverter
            , ProductImageConverter productImageConverter) {
        super(studioRepository, optionConverter, hashtagConverter, productImageConverter);
        this.studioRepository = studioRepository;
    }

    @Override
    public List<StudioEntity> getValidProductList(long companyId) {
        return studioRepository.findAllByCompany_CpIdAndPdStatusNot(companyId, ProductStatus.REMOVE);
    }

    @Override
    public List<StudioEntity> getActiveProductList(long companyId) {
        return studioRepository.findAllByCompany_CpIdAndPdStatus(companyId, ProductStatus.ACTIVE);
    }

    @Override
    protected void updateAdditionalFields(StudioEntity existingEntity, StudioRequest request) {
        Validator.throwIfNull(existingEntity, request);
        existingEntity.updateAdditionalField(request);
    }
}
