package aba3.lucid.domain.user.dto;

import aba3.lucid.domain.product.entity.ProductEntity;
import aba3.lucid.domain.product.entity.ProductImageEntity;
import aba3.lucid.domain.user.entity.UsersEntity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WishListDto implements Serializable {
    
    // 판매업체명
    private String companyName;
    
    // 상품명
    private String productName;
    
    // 상품가격
    private BigInteger price;
    
    // 상품 이미지 리스트
    private List<ProductImageEntity> productImages;
}
