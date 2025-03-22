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
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UsersEntity user = usersRepository.findByUserName(username);

        if(user == null) {
            throw new ApiException(UserCode.USER_NOT_FOUND, "User Not Found");
        }

        return new User(user.getUserName(), user.getUserPassword(), AuthorityUtils.createAuthorityList(user.getUserRole()));
    }
}
