package aba3.lucid.config;

import aba3.lucid.common.exception.ApiException;
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
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        System.out.println("CustomUserDetailsService called");
        UsersEntity usersEntity;
        CompanyEntity companyEntity;
        Optional<UsersEntity> user = usersRepository.findByUserEmail(userEmail);
        Optional<CompanyEntity> company = companyRepository.findByCpEmail(userEmail);

        if(user.isPresent()) {
            usersEntity = user.get();
            System.out.println("usersEntity = " + usersEntity.getUserSocial().getSocialCode());
            if(!usersEntity.getUserSocial().getSocialCode().equals("LOCAL"))    {
                return CustomUserDetails.builder()
                        .email(usersEntity.getUserEmail())
                        .role("ROLE_" + usersEntity.getUserRole())
                        .password(null)
                        .build();
            }
            return CustomUserDetails.builder()
                    .email(usersEntity.getUserEmail())
                    .role("ROLE_" + usersEntity.getUserRole())
                    .password(passwordEncoder.encode(usersEntity.getUserPassword()))
                    .build();
        } else if(company.isPresent())  {
            companyEntity = company.get();
            System.out.println("companyEntity.getCpRole() = " + companyEntity.getCpRole());
            return CustomUserDetails.builder()
                    .email(companyEntity.getCpEmail())
                    .role("ROLE_" + companyEntity.getCpRole())
                    .password(passwordEncoder.encode(companyEntity.getCpPassword()))
                    .build();
        }
        throw new ApiException(UserCode.USER_NOT_FOUND, "User Not Found");
    }
}
