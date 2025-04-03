package aba3.lucid.user.business;

import aba3.lucid.common.annotation.Business;
import aba3.lucid.common.api.API;
import aba3.lucid.common.auth.AuthUtil;
import aba3.lucid.common.status_code.ErrorCode;
import aba3.lucid.domain.user.convertor.UserConvertor;
import aba3.lucid.domain.user.dto.*;
import aba3.lucid.domain.user.entity.UsersEntity;
import aba3.lucid.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Business
@RequiredArgsConstructor
public class UserBusiness {
    
    private final UserConvertor userConvertor;
    private final UserService userService;

    // 유저 회원가입 비즈니스 로직(Converting)
    public API<UserSignupResponse> signup(UserSignupRequest userSignupRequest) {
        if(userSignupRequest == null)   {
            return API.ERROR(ErrorCode.GONE, "요청에 대한 데이터가 없습니다.");
        }
        if(!userSignupRequest.isEmailVerified() && userSignupRequest.isPhoneVerified()) {
            return API.ERROR(ErrorCode.BAD_REQUEST, "이메일, 휴대전화 인증을 진행해주세요.");
        }

        UsersEntity convertedUser = userConvertor.convertUserSignupToUserEntity(userSignupRequest);
        UsersEntity savedUser = userService.signUp(convertedUser);
        UserObject dtoUser = userConvertor.convertEntityToObject(savedUser);

        UserSignupResponse userSignupResponse = UserSignupResponse.builder()
                .data(dtoUser)
                .message("회원가입에 성공하였습니다!")
                .build();

        return API.OK(userSignupResponse);
    }

    // 유저 업데이트 비즈니스 로직(컨버팅)
    public API<UserUpdateResponse> update(UserUpdateRequest userUpdateRequest) {
        if(userUpdateRequest == null)   {
            return API.ERROR(ErrorCode.GONE, "요청에 대한 데이터가 없습니다.");
        }
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        UsersEntity loadUser = userService.findByIdWithThrow(AuthUtil.getUserId());
        loadUser.updateUser(userUpdateRequest, bCryptPasswordEncoder);
        userService.saveByUser(loadUser);
        UserObject dtoUser = userConvertor.convertEntityToObject(loadUser);
        UserUpdateResponse data = UserUpdateResponse.builder()
                .user(dtoUser)
                .build();

        return API.OK(data);
    }
}
