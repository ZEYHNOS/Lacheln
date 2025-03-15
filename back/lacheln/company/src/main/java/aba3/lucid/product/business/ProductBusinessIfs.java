package aba3.lucid.product.business;

import aba3.lucid.domain.product.dto.ProductRequest;
import aba3.lucid.domain.product.dto.ProductResponse;

import java.util.List;

public interface ProductBusinessIfs<REQ extends ProductRequest, RES extends ProductResponse> {

    // 상품(스,드,메) 등록
    RES registerProduct(long companyId, REQ req);


    // 상품(스,드,메) 수정
    RES updateProduct(long companyId, long productId, REQ req);


    // 상품(스,드,메) 삭제
    void deleteProduct(long companyId, long productId);


    // 상품 리스트 보기
    List<RES> getProductList(REQ req);



}
