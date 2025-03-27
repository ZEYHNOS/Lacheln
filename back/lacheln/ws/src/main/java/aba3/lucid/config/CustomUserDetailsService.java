package aba3.lucid.config;

import aba3.lucid.common.exception.ApiException;
import aba3.lucid.common.status_code.UserCode;
import aba3.lucid.domain.user.entity.UsersEntity;
import aba3.lucid.domain.user.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UsersRepository usersRepository;

    @Override
    public CustomUserDetails loadUserByUsername(String userEmail) throws UsernameNotFoundException {
        Optional<UsersEntity> user = usersRepository.findByUserEmail(userEmail);

        if(user.isEmpty()) {
            throw new ApiException(UserCode.USER_NOT_FOUND, "User Not Found");
        }

        UsersEntity userEntity = user.get();

        return CustomUserDetails.builder()
                .email(userEntity.getUserEmail())
                .role("ROLE_" + userEntity.getUserRole())
                .password(userEntity.getUserPassword())
                .build();
    }
}
