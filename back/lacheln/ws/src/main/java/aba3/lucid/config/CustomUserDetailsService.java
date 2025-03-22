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

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UsersRepository usersRepository;

    @Override
    public CustomUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UsersEntity user = usersRepository.findByUserEmail(username);

        if(user == null) {
            throw new ApiException(UserCode.USER_NOT_FOUND, "User Not Found");
        }

        return CustomUserDetails.builder()
                .userId(user.getUserId())
                .email(user.getUserEmail())
                .role("ROLE_" + user.getUserRole())
                .password(user.getUserPassword())
                .build();
    }
}
