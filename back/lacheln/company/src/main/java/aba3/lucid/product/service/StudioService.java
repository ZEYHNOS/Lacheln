package aba3.lucid.product.service;

import aba3.lucid.domain.product.converter.HashtagConverter;
import aba3.lucid.domain.product.converter.OptionConverter;
import aba3.lucid.domain.product.converter.ProductImageConverter;
import aba3.lucid.domain.product.studio.dto.StudioRequest;
import aba3.lucid.domain.product.studio.dto.StudioResponse;
import aba3.lucid.domain.product.studio.entity.StudioEntity;
import aba3.lucid.domain.product.studio.repository.StudioRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class StudioService extends ProductService<StudioEntity, StudioRequest> {

    public StudioService(StudioRepository studioRepository
            , OptionConverter optionConverter
            , HashtagConverter hashtagConverter
            , ProductImageConverter productImageConverter) {
        super(studioRepository, optionConverter, hashtagConverter, productImageConverter);
    }

    @Override
    public List<StudioEntity> getValidProductList(long companyId) {
        return List.of();
    }

    @Override
    public List<StudioEntity> getActiveProductList(long companyId) {
        return List.of();
    }

    @Override
    protected void updateAdditionalFields(StudioEntity existingEntity, StudioRequest request) {

    }
}
