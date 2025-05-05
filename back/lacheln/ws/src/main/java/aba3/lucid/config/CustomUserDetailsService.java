package aba3.lucid.config;

import aba3.lucid.common.auth.CustomUserDetails;
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

    // 해당 메서드는 UserDetailsService에 존재하는 메서드, 구현체의 메서드이기 때문에 재정의 진행
    @Override
    public CustomUserDetails loadUserByUsername(String userEmail) throws UsernameNotFoundException {
        // 요청하는 이메일의 ROLE(소비자, 업체)를 구분할 수 없음으로 Optional로 포장된 객체 생성...
        UsersEntity usersEntity;
        CompanyEntity companyEntity;
        Optional<UsersEntity> user = usersRepository.findByUserEmail(userEmail);
        Optional<CompanyEntity> company = companyRepository.findByCpEmail(userEmail);

        // 유저 정보가 있을 시 로직
        if(user.isPresent()) {
            usersEntity = user.get();
            // 유저가 Local형태로 로그인을 진행할때
            if(!usersEntity.getUserSocial().getSocialCode().equals("LOCAL"))    {
                return CustomUserDetails.builder()
                        .email(usersEntity.getUserEmail())
                        .role(usersEntity.getUserRole())
                        .password(null)
                        .userId(usersEntity.getUserId())
                        .build();
            }
            // 유저가 Social 형태로 로그인을 진행할때
            return CustomUserDetails.builder()
                    .email(usersEntity.getUserEmail())
                    .role(usersEntity.getUserRole())
                    .password(usersEntity.getUserPassword())
                    .userId(usersEntity.getUserId())
                    .build();
        } else if(company.isPresent())  {
            // 유저가 없으면 업체쪽임으로 해당 로직실행
            companyEntity = company.get();
            return CustomUserDetails.builder()
                    .email(companyEntity.getCpEmail())
                    .role(companyEntity.getCpRole())
                    .password(companyEntity.getCpPassword())
                    .companyId(companyEntity.getCpId())
                    .build();
        }
        throw new ApiException(UserCode.USER_NOT_FOUND, "User Not Found");
    }

    public String getUserIdByEmail(String email)   {
        Optional<UsersEntity> usersEntity = usersRepository.findByUserEmail(email);
        return usersEntity.map(UsersEntity::getUserId).orElse(null);
    }
}
