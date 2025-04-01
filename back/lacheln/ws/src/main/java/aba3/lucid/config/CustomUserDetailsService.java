package aba3.lucid.config;

import aba3.lucid.common.exception.ApiException;
import aba3.lucid.common.password.CustomPasswordEncoder;
import aba3.lucid.common.status_code.UserCode;
import aba3.lucid.domain.company.entity.CompanyEntity;
import aba3.lucid.domain.company.repository.CompanyRepository;
import aba3.lucid.domain.user.entity.UsersEntity;
import aba3.lucid.domain.user.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UsersRepository usersRepository;
    private final CompanyRepository companyRepository;

    @Override
    public CustomUserDetails loadUserByUsername(String userEmail) throws UsernameNotFoundException {
        // 암호화 모듈 불러오기
        System.out.println("CustomUserDetailsService called");
        UsersEntity usersEntity;
        CompanyEntity companyEntity;
        Optional<UsersEntity> user = usersRepository.findByUserEmail(userEmail);
        Optional<CompanyEntity> company = companyRepository.findByCpEmail(userEmail);

        // 유저 정보가 있을때
        if(user.isPresent()) {
            usersEntity = user.get();
            // 유저가 Local형태로 로그인을 진행할때
            if(!usersEntity.getUserSocial().getSocialCode().equals("LOCAL"))    {
                return CustomUserDetails.builder()
                        .email(usersEntity.getUserEmail())
                        .role("ROLE_" + usersEntity.getUserRole())
                        .password(null)
                        .userId(usersEntity.getUserId())
                        .build();
            }
            // 유저가 Social 형태로 로그인을 진행할때
            System.out.println("UserId = " + user.get().getUserId());
            return CustomUserDetails.builder()
                    .email(usersEntity.getUserEmail())
                    .role("ROLE_" + usersEntity.getUserRole())
                    .password(usersEntity.getUserPassword())
                    .userId(usersEntity.getUserId())
                    .build();
        } else if(company.isPresent())  {
            // 유저가 없으면 업체쪽임으로 해당 로직실행
            companyEntity = company.get();
            System.out.println("Company ID = " + company.get().getCpId());
            System.out.println("companyEntity.getCpRole() = " + companyEntity.getCpRole());
            return CustomUserDetails.builder()
                    .email(companyEntity.getCpEmail())
                    .role("ROLE_" + companyEntity.getCpRole())
                    .password(companyEntity.getCpPassword())
                    .companyId(companyEntity.getCpId())
                    .build();
        }
        throw new ApiException(UserCode.USER_NOT_FOUND, "User Not Found");
    }
}
