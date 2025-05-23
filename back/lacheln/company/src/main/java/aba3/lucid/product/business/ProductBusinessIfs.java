package aba3.lucid.product.business;

import aba3.lucid.domain.product.dto.ProductRequest;
import aba3.lucid.domain.product.dto.ProductDetailResponse;
import aba3.lucid.domain.product.entity.ProductEntity;

public interface ProductBusinessIfs<REQ extends ProductRequest, RES extends ProductDetailResponse, ENTITY extends ProductEntity> {

    // 상품(스,드,메) 등록
    RES registerProduct(Long companyId, REQ req);


    // 상품(스,드,메) 수정
    RES updateProduct(Long companyId, Long productId, REQ req);


    // 상품(스,드,메) 삭제
    void deleteProduct(Long companyId, Long productId);

}
