package aba3.lucid.product.business;

import aba3.lucid.common.annotation.Business;
import aba3.lucid.common.api.Pagination;
import aba3.lucid.common.api.PaginationConverter;
import aba3.lucid.common.exception.ApiException;
import aba3.lucid.common.image.ImageType;
import aba3.lucid.common.status_code.ErrorCode;
import aba3.lucid.common.validate.Validator;
import aba3.lucid.company.service.CompanyService;
import aba3.lucid.domain.product.dto.ProductDetailResponse;
import aba3.lucid.domain.product.dto.ProductSearchRecord;
import aba3.lucid.domain.product.dto.ProductStatusUpdateRequest;
import aba3.lucid.image.service.ImageService;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    private final PaginationConverter paginationConverter;

    // 상품 검색
    public Page<ProductResponse> getProductList(ProductSearchRecord productSearchRecord) {
        Page<ProductEntity> productEntityPage = productService.getProductPage(productSearchRecord);
        return productConverter.toResponsePage(productEntityPage);
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

    public List<ProductResponse> getValidProductList(Long companyId, ProductStatus status) {
        return productService.getCompanyProductList(companyId, status).stream()
                .map(productConverter::toResponse)
                .toList()
                ;
    }

    // 상품 이미지 업로드
    public List<String> productImagesUpload(Long companyId, List<MultipartFile> images) throws IOException {
        Validator.throwIfInvalidId(companyId);
        if(images.isEmpty()) {
            throw new ApiException(ErrorCode.NULL_POINT);
        }

        CompanyEntity company = companyService.findByIdWithThrow(companyId);
        return imageService.imagesUpload(company, images, ImageType.PRODUCT);
    }

    // 상품 상태 변경
    public void updateStatus(Long companyId, ProductStatusUpdateRequest request) {
        Validator.throwIfInvalidId(companyId);
        Validator.throwIfNull(request);


        CompanyEntity company = companyService.findByIdWithThrow(companyId);
        ProductEntity product = productService.findByIdWithThrow(request.getProductId());

        productService.updateStatus(company, product, request.getStatus());
    }

    public ProductResponse uploadProduct(Long productId, Long companyId) {
        Validator.throwIfInvalidId(productId, companyId);

        ProductEntity product = productService.findByIdWithThrow(productId);
        CompanyEntity company = companyService.findByIdWithThrow(companyId);

        ProductEntity activateProduct = productService.updateStatus(company, product, ProductStatus.ACTIVE);

        return productConverter.toResponse(activateProduct);
    }
}
