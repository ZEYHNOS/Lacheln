package aba3.lucid.product.service;

import aba3.lucid.common.exception.ApiException;
import aba3.lucid.common.status_code.ErrorCode;
import aba3.lucid.common.status_code.ProductErrorCode;
import aba3.lucid.company.service.ImageService;
import aba3.lucid.domain.product.converter.DescriptionConverter;
import aba3.lucid.domain.product.converter.HashtagConverter;
import aba3.lucid.domain.product.converter.OptionConverter;
import aba3.lucid.domain.product.converter.ProductImageConverter;
import aba3.lucid.domain.product.dto.ProductRequest;
import aba3.lucid.domain.product.entity.*;
import aba3.lucid.domain.product.enums.ProductStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public abstract class ProductAbstractService<T extends ProductEntity,R extends ProductRequest>
        implements ProductServiceIfs<T, R> {

    protected final JpaRepository<T, Long> repository;
    protected final OptionConverter optionConverter;
    protected final HashtagConverter hashtagConverter;
    protected final ProductImageConverter productImageConverter;
    protected final DescriptionConverter descriptionConverter;
    protected final ImageService imageService;

    // 상품 저장
    @Override
    public T registerProduct(T entity) {
        log.debug("Product Service {}", entity);

        // TODO 구독한 유저에게 알림 보내기

        return repository.save(entity);
    }

    // 상품 정보 업데이트
    @Override
    @Transactional
    public T updateProduct(T existingEntity, R req) {
        List<OptionEntity> optionEntityList = optionConverter.toEntityList(req.getOptionList(), existingEntity);
        List<HashtagEntity> hashtagEntityList = hashtagConverter.toEntityList(req.getHashTagList(), existingEntity);
        List<ProductImageEntity> productImageEntityList = productImageConverter.toEntityList(req.getImageUrlList(), existingEntity);
        List<ProductDescriptionEntity> productDescriptionEntityList = descriptionConverter.toEntityList(existingEntity, req.getDescriptionList());

        imageService.deleteProductImage(existingEntity.getPdId());

        updateAdditionalFields(existingEntity, req);
        existingEntity.updateFormList(optionEntityList, hashtagEntityList, productImageEntityList, productDescriptionEntityList);


        return repository.save(existingEntity);
    }

    @Override
    public void deleteProduct(T entity) {
        entity.updateStatus(ProductStatus.REMOVE);
        entity.deleteProduct(LocalDateTime.now());
    }

    @Override
    public T findByIdWithThrow(Long productId) {
        return repository.findById(productId)
                .orElseThrow(() -> new ApiException(ProductErrorCode.PRODUCT_NOT_FOUND));
    }

    @Override
    public void throwIfNotCompanyProduct(T entity, Long companyId) {
        if (entity.getCompany().getCpId() != companyId) {
            throw new ApiException(ErrorCode.BAD_REQUEST);
        }
    }

    public abstract List<T> getValidProductList(Long companyId);
    public abstract List<T> getActiveProductList(Long companyId);
    protected abstract void updateAdditionalFields(T existingEntity, R request);


}
