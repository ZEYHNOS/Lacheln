package aba3.lucid.domain.user.service;

import aba3.lucid.common.exception.ApiException;
import aba3.lucid.common.status_code.UserCode;
import aba3.lucid.domain.user.entity.UsersEntity;
import aba3.lucid.domain.user.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UsersRepository usersRepository;

    public UsersEntity findById(String userId) {
        return usersRepository.findById(userId)
                .orElseThrow(() -> new ApiException(UserCode.USER_NOT_FOUND, "User Not Found"));
    }
}
