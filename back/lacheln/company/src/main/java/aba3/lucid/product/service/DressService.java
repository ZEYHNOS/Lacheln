package aba3.lucid.product.service;

import aba3.lucid.common.exception.ApiException;
import aba3.lucid.common.status_code.ProductErrorCode;
import aba3.lucid.domain.product.converter.HashtagConverter;
import aba3.lucid.domain.product.converter.OptionConverter;
import aba3.lucid.domain.product.converter.ProductImageConverter;
import aba3.lucid.domain.product.dress.convertor.DressSizeConverter;
import aba3.lucid.domain.product.dress.dto.DressRequest;
import aba3.lucid.domain.product.dress.entity.DressEntity;
import aba3.lucid.domain.product.dress.entity.DressSizeEntity;
import aba3.lucid.domain.product.dress.repository.DressRepository;
import aba3.lucid.domain.product.entity.HashtagEntity;
import aba3.lucid.domain.product.entity.OptionEntity;
import aba3.lucid.domain.product.entity.ProductImageEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class DressService implements ProductServiceIfs<DressEntity,DressRequest> {

    private final DressRepository dressRepository;

    private final OptionConverter optionConverter;
    private final DressSizeConverter dressSizeConverter;
    private final ProductImageConverter productImageConverter;
    private final HashtagConverter hashtagConverter;

    @Override
    public DressEntity registerProduct(DressEntity entity) {
        return dressRepository.save(entity);
    }

    @Override
    public DressEntity updateProduct(DressEntity existingDress, DressRequest request) {
        // 옵션 Entity 리스트
        List<OptionEntity> optionEntityList = optionConverter.toEntityList(request.getOptionList(), existingDress);

        // 드래스 사이즈 Entity 리스트
        List<DressSizeEntity> dressSizeEntityList = dressSizeConverter.toEntityList(request.getSizeList(), existingDress);

        // 상품 Image Entity 리스트
        List<ProductImageEntity> productImageEntityList = productImageConverter.toEntityList(request.getImageUrlList(), existingDress);

        // 해시 태그 리스트
        List<HashtagEntity> hashtagEntityList = hashtagConverter.toEntityList(request.getHashTagList(), existingDress);

        // 엔티티 정보 업데이트
        existingDress.updateFormRequest(request);
        existingDress.updateFormList(dressSizeEntityList, productImageEntityList, optionEntityList, hashtagEntityList);

        // DB 저장 후 반환
        return dressRepository.save(existingDress);
    }

    @Override
    public void deleteProduct(DressEntity entity) {
        dressRepository.delete(entity);
    }

    @Override
    public List<DressEntity> getProductList(long companyId) {
        return dressRepository.findAllByCompany_CpId(companyId);
    }

    @Override
    public DressEntity findByIdWithThrow(long id) {
        return dressRepository.findById(id)
                .orElseThrow(() -> new ApiException(ProductErrorCode.PRODUCT_NOT_FOUND));
    }
}
