package aba3.lucid.product.business;

import aba3.lucid.common.annotation.Business;
import aba3.lucid.common.ifs.ConverterIfs;
import aba3.lucid.common.validate.Validator;
import aba3.lucid.company.service.CompanyService;
import aba3.lucid.domain.company.entity.CompanyEntity;
import aba3.lucid.domain.company.enums.CompanyCategory;
import aba3.lucid.domain.product.dto.ProductRequest;
import aba3.lucid.domain.product.dto.ProductResponse;
import aba3.lucid.domain.product.entity.ProductEntity;
import aba3.lucid.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@Business
@RequiredArgsConstructor
public abstract class ProductBusiness<REQ extends ProductRequest, RES extends ProductResponse, ENTITY extends ProductEntity>
        implements ProductBusinessIfs<REQ, RES, ENTITY> {

    private final ProductService<ENTITY, REQ> productService;
    private final ConverterIfs<ENTITY, REQ, RES> converterIfs;
    private final CompanyService companyService;

    @Override
    public RES registerProduct(long companyId, REQ req) {
        Validator.throwIfInvalidId(companyId);
        Validator.throwIfNull(req);

        log.info("Request HashTag {} ", req.getHashTagList());
        CompanyEntity companyEntity = companyService.findByIdAndMatchCategoryWithThrow(companyId, getCategory());
        log.info("Request {}", req);

        ENTITY entity = converterIfs.toEntity(req, companyEntity);
        log.info("hashTag : {} ", entity.getHashtagList());
        ENTITY newEntity = productService.registerProduct(entity);
        log.info("newEntity : {}", newEntity);

        return converterIfs.toResponse(newEntity);
    }

    @Override
    public RES updateProduct(long companyId, long productId, REQ req) {
        Validator.throwIfInvalidId(companyId, productId);
        Validator.throwIfNull(req);

        companyService.findByIdAndMatchCategoryWithThrow(companyId, getCategory());

        ENTITY productEntity = productService.findByIdWithThrow(productId);
        productService.throwIfNotCompanyProduct(productEntity, companyId);

        ENTITY updateEntity = productService.updateProduct(productEntity, req);
        return converterIfs.toResponse(updateEntity);
    }

    @Override
    public void deleteProduct(long companyId, long productId) {
        Validator.throwIfInvalidId(companyId, productId);

        ENTITY productEntity = productService.findByIdWithThrow(productId);
        productService.throwIfNotCompanyProduct(productEntity, companyId);

        productService.deleteProduct(productEntity);
    }

    public abstract List<RES> getProductList(long companyId);
    public abstract CompanyCategory getCategory();
}
