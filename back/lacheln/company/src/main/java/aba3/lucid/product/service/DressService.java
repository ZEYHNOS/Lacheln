package aba3.lucid.product.service;

import aba3.lucid.common.exception.ApiException;
import aba3.lucid.common.status_code.ProductErrorCode;
import aba3.lucid.domain.product.dress.entity.DressEntity;
import aba3.lucid.domain.product.dress.repository.DressRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DressService implements ProductServiceIfs<DressEntity> {

    private final DressRepository dressRepository;

    @Override
    public DressEntity registerProduct(DressEntity entity) {
        return dressRepository.save(entity);
    }

    @Override
    public DressEntity updateProduct(DressEntity entity) {
        return dressRepository.save(entity);
    }

    @Override
    public void deleteProduct(DressEntity entity) {
        dressRepository.delete(entity);
    }

    @Override
    public List<DressEntity> getProductList(long id) {
        return List.of();
    }

    @Override
    public DressEntity findByIdWithThrow(long id) {
        return dressRepository.findById(id)
                .orElseThrow(() -> new ApiException(ProductErrorCode.PRODUCT_NOT_FOUND));
    }
}
