package aba3.lucid.product.business;

import aba3.lucid.common.annotation.Business;
import aba3.lucid.common.exception.ApiException;
import aba3.lucid.common.image.ImageType;
import aba3.lucid.common.status_code.ErrorCode;
import aba3.lucid.common.validate.Validator;
import aba3.lucid.company.service.CompanyService;
import aba3.lucid.company.service.ImageService;
import aba3.lucid.domain.company.entity.CompanyEntity;
import aba3.lucid.domain.company.enums.CompanyCategory;
import aba3.lucid.domain.packages.converter.PackageToProductConverter;
import aba3.lucid.domain.packages.dto.ProductPackageInsertResponse;
import aba3.lucid.domain.packages.entity.PackageEntity;
import aba3.lucid.domain.packages.entity.PackageToProductEntity;
import aba3.lucid.domain.product.converter.ProductConverter;
import aba3.lucid.domain.product.dto.option.ProductResponse;
import aba3.lucid.domain.product.entity.ProductEntity;
import aba3.lucid.domain.product.enums.ProductStatus;
import aba3.lucid.packages.service.PackageService;
import aba3.lucid.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Slf4j
@Business
@RequiredArgsConstructor
public class ProductBusiness {

    private final ProductService productService;
    private final PackageService packageService;
    private final CompanyService companyService;
    private final ImageService imageService;

    private final ProductConverter productConverter;
    private final PackageToProductConverter packageToProductConverter;

    // 특정 업체의 상품 리스트 todo 로직 제대로 짜기
    public List<ProductResponse> getProductList(CompanyCategory category, int minimum, int maximum, boolean isDesc) {
        List<ProductEntity> productEntityList = productService.getProductList(category, minimum, maximum, isDesc);

        return productConverter.toResponseList(productEntityList);
    }


    // 상품을 패키지에 등록하기
    public ProductPackageInsertResponse packageRegister(Long packageId, Long companyId, Long productId) {
        Validator.throwIfInvalidId(packageId, companyId);

        // 패키지 존재 유무 파악 및 해당 업체가 포함되는지
        PackageEntity packageEntity = packageService.findByPackIdAndCompanyIdWithThrow(packageId, companyId);
        ProductEntity productEntity = productService.findByIdWithThrow(productId);


        // DTO -> Entity
        PackageToProductEntity packageToProductEntity = packageToProductConverter.toEntity(packageEntity, productEntity);
        // 패키지에 상품 등록하기
        PackageToProductEntity newPackageToProductEntity = packageService.productPackageInsert(packageToProductEntity, companyId, packageEntity, productEntity);

        // Entity -> DTO
        return productConverter.toResponse(newPackageToProductEntity);
    }

    public List<ProductResponse> getValidProductList(Long companyId) {
        return productService.getCompanyProductList(companyId).stream()
                // 삭제된 상품 제외하고 보여주기
                .filter(it -> !it.getPdStatus().equals(ProductStatus.REMOVE))
                .map(productConverter::toResponse)
                .toList()
                ;
    }

    public List<String> productImagesUpload(Long companyId, List<MultipartFile> images) throws IOException {
        Validator.throwIfInvalidId(companyId);
        if(images.isEmpty()) {
            throw new ApiException(ErrorCode.NULL_POINT);
        }

        CompanyEntity company = companyService.findByIdWithThrow(companyId);
        return imageService.imagesUpload(company, images, ImageType.PRODUCT_IMAGE);
    }
}
