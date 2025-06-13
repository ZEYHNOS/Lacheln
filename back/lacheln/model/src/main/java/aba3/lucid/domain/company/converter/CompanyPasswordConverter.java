package aba3.lucid.domain.company.converter;

import aba3.lucid.domain.company.dto.CompanyPasswordUpdateRequest;
import aba3.lucid.domain.company.dto.CompanyPasswordUpdateResponse;
import aba3.lucid.domain.company.entity.CompanyEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CompanyPasswordConverter {
    private final BCryptPasswordEncoder passwordEncoder;

    public CompanyEntity toEntity(CompanyPasswordUpdateRequest request) {
        if(request == null) {
            return null;
        }
        return CompanyEntity.builder()
                .cpPassword(passwordEncoder.encode(request.getNewPassword()))
                .build();
    }

    public CompanyPasswordUpdateResponse toResponse(CompanyEntity companyEntity) {
        if(companyEntity == null) {
            return null;
        }
        return CompanyPasswordUpdateResponse.builder()
                .success(true)
                .message("비밀번호가 성공적으로 변경되었습니다.")
                .build();
    }
}
