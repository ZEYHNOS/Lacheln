package aba3.lucid.product.service;

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

    public MakeupService(MakeupRepository makeupRepository, OptionConverter optionConverter, HashtagConverter hashtagConverter, ProductImageConverter imageConverter) {
        super(makeupRepository, optionConverter, hashtagConverter, imageConverter);
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
        existingEntity.updateAdditionalField(request);
    }
}

