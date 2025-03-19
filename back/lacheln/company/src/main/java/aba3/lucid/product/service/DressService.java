package aba3.lucid.product.service;

import aba3.lucid.domain.product.converter.HashtagConverter;
import aba3.lucid.domain.product.converter.OptionConverter;
import aba3.lucid.domain.product.converter.ProductImageConverter;
import aba3.lucid.domain.product.dress.convertor.DressSizeConverter;
import aba3.lucid.domain.product.dress.dto.DressRequest;
import aba3.lucid.domain.product.dress.entity.DressEntity;
import aba3.lucid.domain.product.dress.entity.DressSizeEntity;
import aba3.lucid.domain.product.dress.repository.DressRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional
public class DressService extends ProductService<DressEntity, DressRequest> {

    private final DressRepository dressRepository;
    private final DressSizeConverter dressSizeConverter;

    public DressService(DressRepository dressRepository,
                        OptionConverter optionConverter,
                        HashtagConverter hashtagConverter,
                        ProductImageConverter productImageConverter,
                        DressSizeConverter dressSizeConverter) {
        super(dressRepository, optionConverter, hashtagConverter, productImageConverter);
        this.dressRepository = dressRepository;
        this.dressSizeConverter = dressSizeConverter;
    }

    @Override
    public List<DressEntity> getProductList(long companyId) {
        return dressRepository.findAllByCompany_CpId(companyId);
    }

    @Override
    protected void updateAdditionalFields(DressEntity existingEntity, DressRequest request) {
        List<DressSizeEntity> dressSizeEntityList = dressSizeConverter.toEntityList(request.getSizeList(), existingEntity);
        existingEntity.updateAdditionalField(request, dressSizeEntityList);
    }
}

