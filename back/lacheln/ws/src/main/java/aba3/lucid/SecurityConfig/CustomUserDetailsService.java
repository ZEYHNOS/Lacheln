package aba3.lucid.SecurityConfig;

import aba3.lucid.domain.user.entity.UsersEntity;
import aba3.lucid.domain.user.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    // DB에 접근할 Repository DI
    private final UsersRepository usersRepository;

    // 상속받은 UserDetailsService에 있는 loadUserByUsername 메서드 재정의
    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        // User 가져오기
        UsersEntity user = usersRepository.findByUserName(userName);
        
        // 만약 없으면 Exception 발생
        if(user == null)    {
            throw new UsernameNotFoundException("User not found");
        }

        // 가져온 user에서 Role을 저장
        String role = user.getUserRole();

        // UserDetails 객체에 알맞게 user 정보 저장
        return User.builder()
                .username(user.getUserName())
                .password(user.getUserPassword())
                .authorities(new SimpleGrantedAuthority(role))
                .build();
    }
}
