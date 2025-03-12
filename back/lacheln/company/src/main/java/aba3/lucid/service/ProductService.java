package aba3.lucid.service;

import aba3.lucid.common.exception.ApiException;
import aba3.lucid.common.status_code.ErrorCode;
import aba3.lucid.domain.company.entity.CompanyEntity;
import aba3.lucid.domain.company.enums.CompanyCategory;
import aba3.lucid.domain.company.enums.CompanyStatus;
import aba3.lucid.domain.product.dress.entity.DressEntity;
import aba3.lucid.domain.product.dress.dto.DressRequest;
import aba3.lucid.domain.product.dress.repository.DressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final DressRepository dressRepository;

    // 드레스 업체 상품 등록
    public DressEntity dressRegister(DressEntity entity) {
        return dressRepository.save(entity);
    }


    // 드레스 업체 상품 수정
    public DressEntity dressUpdate(long id, DressRequest dressRequest) {
        DressEntity entity = findByDressIdWithThrow(id);

        // 드레스 Entity 변경 로직
        entity.changeColor(dressRequest.getColor()); // 색 변경
        entity.changeInAvailable(dressRequest.getInAvailable()); // 내부 촬영 여부 변경
        entity.changeOutAvailable(dressRequest.getOutAvailable()); // 외부 촬영 여부 변경

        // 상품 Entity 변경 로직
        entity.changeProductInfo(dressRequest); // 상품 변경 (이름, 가격 등등)

        // TODO Option 변경 로직

        // TODO Option Detail 변경 로직

        return dressRepository.save(entity);
    }


    // 드레스 업체 상품 삭제하기
    public void dressDelete(long dressId) {
        // 상품이 존재하는지
        if (!dressRepository.existsById(dressId)) {
            // TODO ErrorCode 변경하기
            throw new ApiException(ErrorCode.BAD_REQUEST, "해당 상품은 존재하지 않습니다.");
        }
        DressEntity entity = findByDressIdWithThrow(dressId);

        // TODO 삭제하기
        CompanyEntity company = getGarbageEntity();
        if (!company.equals(entity.getProduct())) {
            // TODO ErrorCode 변경하기
            throw new ApiException(ErrorCode.BAD_REQUEST, "해당 업체 상품이 아님");
        }

        dressRepository.delete(entity);
    }


    // 드레스 상품 리스트 보기
    public List<DressEntity> getDressEntityList(long companyId) {
        return dressRepository.findAllByProduct_Company_cpId(companyId);
    }


    // 드레스 업체 상품 찾기
    public DressEntity findByDressIdWithThrow(long id) {
        return dressRepository.findById(id)
                .orElseThrow(() -> new ApiException(ErrorCode.BAD_REQUEST, "해당 상품을 찾을 수 없습니다."));
    }


    // Garbage 데이터
    private CompanyEntity getGarbageEntity() {
        return CompanyEntity.builder()
                .cpId(1)
                .cpEmail("shs00925@naver.com")
                .cpPassword("123456789")
                .cpName("손휘성")
                .cpRepName("위대한현준")
                .cpMainContact("대.현.준")
                .cpAddress("대구광역시 북구 영진ㅈ문대")
                .cpPostalCode("54587")
                .cpBnRegNo("51515151515151515151")
                .cpMos("1515153")
                .cpStatus(CompanyStatus.ACTIVATE)
                .cpProfile("/static/company/default/profile")
                .cpExplain("Explain")
                .cpCategory(CompanyCategory.D)
                .cpContact("ㅎㅇ")
                .cpFax("5215")
                .build()
                ;
    }
}
