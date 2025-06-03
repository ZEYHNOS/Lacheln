package aba3.lucid.domain.company.converter;

import aba3.lucid.domain.company.dto.SnsRequest;
import aba3.lucid.domain.company.dto.SnsResponse;
import aba3.lucid.domain.company.entity.CompanyEntity;
import aba3.lucid.domain.company.entity.SocialEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SnsConverter {

    public SocialEntity toEntity (SnsRequest request, CompanyEntity company) {
        if(request == null){
            return  null;
        }
        return SocialEntity.builder()
                .company(company)
                .snsName(request.getName())
                .snsUrl(request.getUrl())
                .build();
    }

    public SnsResponse toResponse(SocialEntity entity) {
        if(entity == null){
            return null;
        }

        return SnsResponse.builder()
                .snsId(entity.getSnsId())
                .snsName(entity.getSnsName())
                .snsUrl(entity.getSnsUrl())
                .build();

    }

}
