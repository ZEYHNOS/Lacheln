package aba3.lucid.securityconfig;

import aba3.lucid.domain.company.entity.CompanyEntity;
import aba3.lucid.domain.company.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomCompanyDetailsService implements UserDetailsService {
    // DB에 접근할 Repository DI
    private final CompanyRepository companyRepository;

    // 상속받은 UserDetailsService에 있는 loadUserByUsername 메서드 재정의
    @Override
    public UserDetails loadUserByUsername(String companyName) throws UsernameNotFoundException {
        // Company 가져오기
        CompanyEntity company = companyRepository.findByCpName(companyName);

        // 만약 없으면 Exception 발생
        if(company == null) {
            throw new UsernameNotFoundException("Company not found");
        }

        // 가져온 Company의 Role 저장
        String role = company.getCpRole();

        // UserDetails 객체에 알맞게 Company 정보 저장
        return CustomUserDetails.builder()
                .username(company.getCpName())
                .password(company.getCpPassword())
                .userType("COMPANY")
                .build();
    }
}
