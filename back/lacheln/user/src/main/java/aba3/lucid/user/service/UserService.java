package aba3.lucid.user.service;

import aba3.lucid.common.exception.ApiException;
import aba3.lucid.common.status_code.ErrorCode;
import aba3.lucid.domain.user.entity.UsersEntity;
import aba3.lucid.domain.user.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UsersRepository usersRepository;

    // saveByUser와 중복되는 느낌이 있지만 추가적인 로직이 있을 수 있다고 생각해서 따로 구분함
    public UsersEntity signUp(UsersEntity usersEntity) throws ApiException {
        return saveByUser(usersEntity);
    }
    
    // 유저 정보를 바탕으로 저장
    public UsersEntity saveByUser(UsersEntity usersEntity) {
        return usersRepository.save(usersEntity);
    }

    // ID로 유저를 찾음
    public UsersEntity findByIdWithThrow(String userId) {
        return usersRepository.findById(userId).orElseThrow(() ->
                new ApiException(ErrorCode.NULL_POINT, "해당하는 유저가 존재하지 않습니다."));
    }

    // 이메일로 유저를 찾음
    public Optional<UsersEntity> findByEmail(String userEmail) {
        return usersRepository.findByUserEmail(userEmail);
    }

    // 이메일로 유저를 찾음(예외처리 추가)
    public UsersEntity findByEmailWithThrow(String userEmail) {
        return usersRepository.findByUserEmail(userEmail).orElseThrow(() ->
                new ApiException(ErrorCode.NULL_POINT, "해당하는 유저가 존재하지 않습니다."));
    }

    public void deleteById(String userId) {
        usersRepository.deleteById(userId);
    }
}
