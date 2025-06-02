package aba3.lucid.domain.cart.convertor;

import aba3.lucid.common.annotation.Converter;
import aba3.lucid.domain.cart.dto.*;
import aba3.lucid.domain.cart.entity.CartDetailEntity;
import aba3.lucid.domain.cart.entity.CartEntity;
import aba3.lucid.domain.user.entity.UsersEntity;

import java.util.ArrayList;
import java.util.List;

@Converter
public class CartConvertor {

    // 모든 상품(개별, 패키지)의 Entity -> Dto 변환
    public List<CartAllResponse> convertToCartAllResponses(List<CartEntity> entities) {
        return entities
                .stream()
                .map(this::convertToCartAllResponse)
                .toList();
    }

    // 상품, 패키지 Entity -> Dto 변환
    public CartAllResponse convertToCartAllResponse(CartEntity cart) {
        if(cart.getPackId() == null)    {
            return CartAllResponse.builder()
                    .cartId(cart.getCartId())
                    .cpId(cart.getCpId())
                    .cpName(cart.getCpName())
                    .pdId(cart.getPdId())
                    .pdName(cart.getPdName())
                    .price(cart.getPrice())
                    .cartQuantity(cart.getCartQuantity())
                    .pdImageUrl(cart.getPdImageUrl())
                    .startTime(cart.getStartDatetime())
                    .taskTime(cart.getTaskTime())
                    .cartDetails(convertToDtoByPd((cart.getCartDetails())))
                    .build();
        } else {
            return CartAllResponse.builder()
                    .packId(cart.getPackId())
                    .packName(cart.getPackName())
                    .cartId(cart.getCartId())
                    .cpId(cart.getCpId())
                    .cpName(cart.getCpName())
                    .pdId(cart.getPdId())
                    .pdName(cart.getPdName())
                    .price(cart.getPrice())
                    .cartQuantity(cart.getCartQuantity())
                    .pdImageUrl(cart.getPdImageUrl())
                    .startTime(cart.getStartDatetime())
                    .taskTime(cart.getTaskTime())
                    .cartDetails(convertToDtoByPd((cart.getCartDetails())))
                    .build();
        }
    }


    // 개별 상품 Dto -> Entity 변환
    public CartEntity convertToEntityByPd(UsersEntity user, CartAddProductRequest request)  {
        return CartEntity.builder()
                .users(user)
                .pdId(request.getPdId())
                .pdName(request.getPdName())
                .pdImageUrl(request.getPdImageUrl())
                .price(request.getPdPrice())
                .startDatetime(request.getStartDateTime())
                .cpId(request.getCpId())
                .cpName(request.getCpName())
                .taskTime(request.getTaskTime())
                .cartQuantity(request.getCartQuantity())
                .build();
    }

    // 개별 상품의 옵션 Dto -> Entity 변환
    public CartDetailEntity convertToEntityByPd(CartEntity cart, CartOptionDetail request)   {
        return CartDetailEntity.builder()
                .cart(cart)
                .optionName(request.getOptionName())
                .optionDetailName(request.getOptionDetailName())
                .optionPrice(request.getPrice())
                .cartDtQuantity(request.getQuantity())
                .optionTaskTime(request.getOptionTaskTime())
                .optionId(request.getOptionId())
                .build();
    }

    // 개별 상품의 Entity -> Dto 변환
    public CartProductResponse convertToDtoByPd(CartEntity cart, List<CartDetailEntity> request)   {
        List<CartDetailResponse> response = convertToDtoByPd(request);
        return CartProductResponse.builder()
                .startTime(cart.getStartDatetime())
                .cartQuantity(cart.getCartQuantity())
                .price(cart.getPrice())
                .cpId(cart.getCpId())
                .cpName(cart.getCpName())
                .pdImageUrl(cart.getPdImageUrl())
                .pdName(cart.getPdName())
                .cartId(cart.getCartId())
                .taskTime(cart.getTaskTime())
                .pdId(cart.getPdId())
                .cartDetails(response)
                .build();
    }

    // 개별 상품 옵션의 Entity -> Dto 변환
    public List<CartDetailResponse> convertToDtoByPd(List<CartDetailEntity> entities)   {
        return entities.stream()
                .map(details -> CartDetailResponse.builder()
                        .optionDetailName(details.getOptionDetailName())
                        .optionName(details.getOptionName())
                        .quantity(details.getCartDtQuantity())
                        .detailPrice(details.getOptionPrice())
                        .detailTaskTime(details.getOptionTaskTime())
                        .cartDtId(details.getCartDtId())
                        .build())
                .toList();
    }

    // 개별 상품의 EntityList -> DtoList로 변환
    public List<CartProductResponse> convertToResponses(List<CartEntity> cartEntityList)  {
        return cartEntityList
                .stream()
                .map(cart -> CartProductResponse.builder()
                        .cartId(cart.getCartId())
                        .cartQuantity(cart.getCartQuantity())
                        .cartDetails(convertToResponseDetails(cart.getCartDetails()))
                        .pdId(cart.getPdId())
                        .cpId(cart.getCpId())
                        .cpName(cart.getCpName())
                        .price(cart.getPrice())
                        .pdName(cart.getPdName())
                        .pdImageUrl(cart.getPdImageUrl())
                        .startTime(cart.getStartDatetime())
                        .taskTime(cart.getTaskTime())
                        .build())
                .toList();
    }

    // 개별 상품 옵션을 EntityList -> DtoList로 변환
    public List<CartDetailResponse> convertToResponseDetails(List<CartDetailEntity> cartDetailEntityList)  {
        return cartDetailEntityList
                .stream()
                .map(details -> CartDetailResponse.builder()
                        .cartDtId(details.getCartDtId())
                        .quantity(details.getCartDtQuantity())
                        .detailPrice(details.getOptionPrice())
                        .detailTaskTime(details.getOptionTaskTime())
                        .optionName(details.getOptionName())
                        .optionDetailName(details.getOptionDetailName())
                        .build())
                .toList();
    }

    // 패키지 Dto -> Entity로 변환
    public List<CartEntity> convertToEntityByPackage(UsersEntity user, CartAddPackageRequest request)   {
        List<CartEntity> entities = new ArrayList<>();
        List<CartDetailEntity> optionDetails = new ArrayList<>();
        List<CartAddProductRequest> products = request.getCartAddProductRequest();

        for(CartAddProductRequest product : products) {
            CartEntity cart = CartEntity.builder()
                    .users(user)
                    .packId(request.getPackId())
                    .packName(request.getPackName())
                    .taskTime(request.getTaskTime())
                    .pdImageUrl(request.getPackImageUrl())
                    .price(request.getDiscountPrice())
                    .startDatetime(request.getStartTime())
                    .pdId(product.getPdId())
                    .cpId(product.getCpId())
                    .pdName(product.getPdName())
                    .cpName(product.getCpName())
                    .cartQuantity(product.getCartQuantity())
                    .build();
            for(CartOptionDetail optionDetail : product.getOptionDetails()) {
                CartDetailEntity detail = CartDetailEntity.builder()
                        .cart(cart)
                        .optionDetailId(optionDetail.getOptionDetailId())
                        .optionDetailName(optionDetail.getOptionDetailName())
                        .optionName(optionDetail.getOptionName())
                        .optionId(optionDetail.getOptionId())
                        .cartDtQuantity(optionDetail.getQuantity())
                        .optionPrice(optionDetail.getPrice())
                        .optionTaskTime(optionDetail.getOptionTaskTime())
                        .build();
                optionDetails.add(detail);
            }
            cart.addCartDetail(optionDetails);
            entities.add(cart);
        }
        return entities;
    }
    
    // 패키지 Entity -> Dto 변환
    public CartPackageResponse convertToDtoByPackage(List<CartEntity> request)   {
        List<CartProductResponse> products = new ArrayList<>();
        CartEntity pack = request.get(0);

        for(CartEntity cart : request)  {
            List<CartDetailResponse> optionDetails = new ArrayList<>();
            for(CartDetailEntity optionDetail : cart.getCartDetails())  {
                CartDetailResponse options = CartDetailResponse.builder()
                        .optionDetailName(optionDetail.getOptionDetailName())
                        .optionName(optionDetail.getOptionName())
                        .detailPrice(optionDetail.getOptionPrice())
                        .quantity(optionDetail.getCartDtQuantity())
                        .cartDtId(optionDetail.getCartDtId())
                        .detailTaskTime(optionDetail.getOptionTaskTime())
                        .build();
                optionDetails.add(options);
            }
            CartProductResponse product = CartProductResponse.builder()
                    .cartId(cart.getCartId())
                    .cartQuantity(cart.getCartQuantity())
                    .price(cart.getPrice())
                    .pdName(cart.getPdName())
                    .pdId(cart.getPdId())
                    .pdImageUrl(cart.getPdImageUrl())
                    .cpId(cart.getCpId())
                    .cpName(cart.getCpName())
                    .startTime(cart.getStartDatetime())
                    .taskTime(cart.getTaskTime())
                    .cartDetails(optionDetails)
                    .build();
            products.add(product);
        }

        return CartPackageResponse.builder()
                .packId(pack.getPackId())
                .packName(pack.getPackName())
                .discountPrice(pack.getPrice())
                .packImageUrl(pack.getPdImageUrl())
                .startTime(pack.getStartDatetime())
                .taskTime(pack.getTaskTime())
                .cartProductResponses(products)
                .build();
    }
}
