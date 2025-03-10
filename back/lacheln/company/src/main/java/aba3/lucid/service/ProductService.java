package aba3.lucid.service;

import aba3.lucid.common.exception.ApiException;
import aba3.lucid.common.status_code.ErrorCode;
import aba3.lucid.domain.product.DressEntity;
import aba3.lucid.dto.product.dress.DressRequest;
import aba3.lucid.repository.product.DressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final DressRepository dressRepository;

    public DressEntity dressRegister(DressEntity entity) {
        return dressRepository.save(entity);
    }



    public DressEntity dressUpdate(long id, DressRequest dressRequest) {
        DressEntity entity = findByIdWithThrow(id);

        // TODO 변경 로직
        entity.changeColor(dressRequest.getColor()); // 색 변경
        entity.changeInAvailable(dressRequest.getInAvailable()); // 내부 촬영 여부 변경
        entity.changeOutAvailable(dressRequest.getOutAvailable()); // 외부 촬영 여부 변경
        entity.changeProductInfo(dressRequest); // 상품 변경 (이름, 가격 등등)

        return dressRepository.save(entity);
    }


    public DressEntity findByIdWithThrow(long id) {
        return dressRepository.findById(id)
                .orElseThrow(() -> new ApiException(ErrorCode.BAD_REQUEST, "해당 상품을 찾을 수 없습니다."));
    }
}
