package aba3.lucid.product.service;

import aba3.lucid.domain.product.entity.ProductEntity;

import java.util.List;

public interface ProductServiceIfs<T,REQ> {

    // 상품 등록
    T registerProduct(T entity);

    // 상품 수정
    T updateProduct(T entity, REQ req);

    // 상품 삭제
    void deleteProduct(T entity);

    // 상품 리스트
    List<T> getProductList(long companyId);

    // 아이디로 entity 가지고 오기
    T findByIdWithThrow(long productId);

}
