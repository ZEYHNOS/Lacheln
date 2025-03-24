package aba3.lucid.product.business;

import aba3.lucid.common.annotation.Business;
import aba3.lucid.common.exception.ApiException;
import aba3.lucid.common.status_code.ErrorCode;
import aba3.lucid.common.validate.Validator;
import aba3.lucid.domain.company.enums.CompanyCategory;
import aba3.lucid.domain.packages.converter.PackageToProductConverter;
import aba3.lucid.domain.packages.dto.ProductPackageInsertResponse;
import aba3.lucid.domain.packages.entity.PackageEntity;
import aba3.lucid.domain.packages.entity.PackageToProductEntity;
import aba3.lucid.domain.product.converter.ProductConverter;
import aba3.lucid.domain.product.dto.option.ProductResponse;
import aba3.lucid.domain.product.entity.ProductEntity;
import aba3.lucid.packages.service.PackageService;
import aba3.lucid.product.service.ProductService;
import aba3.lucid.rabbitmq.Producer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@Business
@RequiredArgsConstructor
public class ProductBusiness {

    private final ProductService productService;
    private final PackageService packageService;

    private final ProductConverter productConverter;
    private final PackageToProductConverter packageToProductConverter;

    private final Producer producer;

    public List<ProductResponse> getProductList(CompanyCategory category, int minimum, int maximum, boolean isDesc) {
        List<ProductEntity> productEntityList = productService.getProductList(category, minimum, maximum, isDesc);

        return productConverter.toResponseList(productEntityList);
    }

    public void sendMessage(String message) {
        producer.sendMessage(message);
    }


    public ProductPackageInsertResponse packageRegister(long packageId, long companyId, long productId) {
        Validator.throwIfInvalidId(packageId, companyId);

        // 패키지 존재 유무 파악 및 해당 업체가 포함되는지
        PackageEntity packageEntity = packageService.findByPackIdAndCompanyIdWithThrow(packageId, companyId);

        // 이미 해당 업체가 상품을 등록했는지


        // 등록하려는 상품이 해당 업체의 것인지 TODO 리팩토링
        ProductEntity productEntity = productService.findByIdWithThrow(productId);
        if (productEntity.getCompany().getCpId() != companyId) {
            throw new ApiException(ErrorCode.BAD_REQUEST);
        }

        // 패키지에 상품 등록하기
        PackageToProductEntity packageToProductEntity = packageToProductConverter.toEntity(packageEntity, productEntity);
        PackageToProductEntity newPackageToProductEntity = packageService.productPackageInsert(packageToProductEntity);

        return productConverter.toResponse(newPackageToProductEntity);
    }
}
