package aba3.lucid.user.business;

import aba3.lucid.common.annotation.Business;
import aba3.lucid.common.api.API;
import aba3.lucid.common.auth.AuthUtil;
import aba3.lucid.common.exception.ApiException;
import aba3.lucid.common.status_code.ErrorCode;
import aba3.lucid.domain.user.convertor.UserConvertor;
import aba3.lucid.domain.user.dto.*;
import aba3.lucid.domain.user.entity.UsersEntity;
import aba3.lucid.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Slf4j
@Business
@RequiredArgsConstructor
public class UserBusiness {


    private final UserConvertor userConvertor;
    private final UserService userService;

    // 유저 회원가입 비즈니스 로직(Converting)
    public API<UserSignupResponse> signup(UserSignupRequest userSignupRequest) {
        // 요청 데이터 확인
        if(userSignupRequest == null)   {
            return API.ERROR(ErrorCode.GONE, "요청에 대한 데이터가 없습니다.");
        }
        
        // 이메일 인증 및 휴대전화 인증 여부 확인 
        if(!userSignupRequest.isEmailVerified() && userSignupRequest.isPhoneVerified()) {
            return API.ERROR(ErrorCode.BAD_REQUEST, "이메일, 휴대전화 인증을 진행해주세요.");
        }
        
        // 정보 추출 및 Response로 변환
        UsersEntity convertedUser = userConvertor.convertUserSignupToUserEntity(userSignupRequest);
        UsersEntity savedUser = userService.signUp(convertedUser);
        UserDto dtoUser = userConvertor.convertEntityToDto(savedUser);

        UserSignupResponse userSignupResponse = UserSignupResponse.builder()
                .data(dtoUser)
                .message("회원가입에 성공하였습니다!")
                .build();

        return API.OK(userSignupResponse);
    }

    // 유저 업데이트 비즈니스 로직(컨버팅)
    public API<UserUpdateResponse> update(UserUpdateRequest userUpdateRequest) {
        // 업데이터 정보 확인
        if(userUpdateRequest == null)   {
            throw new ApiException(ErrorCode.GONE, "요청에 대한 데이터가 없습니다.");
        }
        
        // 현재 유저 세션정보 추출 및 소셜 여부 확인
        UsersEntity loadUser = userService.findByIdWithThrow(AuthUtil.getUserId());
        if(!loadUser.getUserSocial().getSocialCode().equals("LOCAL") && userUpdateRequest.getPassword() != null)   {
            throw new ApiException(ErrorCode.BAD_REQUEST, "소셜계정은 비밀번호 변경이 불가합니다.");
        }
        
        // 암호화 모듈 로드 및 암호화된 비밀번호와 함께 유저 정보 업데이트 
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        loadUser.updateUser(userUpdateRequest, bCryptPasswordEncoder);
        UsersEntity saved = userService.saveByUser(loadUser);
        UserDto dtoUser = userConvertor.convertEntityToDto(saved);
        UserUpdateResponse data = UserUpdateResponse.builder()
                .user(dtoUser)
                .build();

        return API.OK(data);
    }

    // 유저 프로필 조회
    public API<UserCheckResponse> getUser(String userId) {
        // 현재 세션 존재 여부 확인
        if(AuthUtil.getAuth() == null)  {
            throw new ApiException(ErrorCode.UNAUTHORIZED, "세션이 존재하지 않습니다.");
        }
        
        // 유저 정보 추출 및 프로필에 저장되는 정보 반환
        UsersEntity user = userService.findByIdWithThrow(userId);
        UserCheckResponse responseData = userConvertor.entityToCheckResponse(user);

        return API.OK(responseData);
    }

    //유저 프로필 이메일로 조회
    public API<UserCheckResponse> checkUserByEmail( String userEmail) {
        if(userEmail == null) {
            throw  new ApiException(ErrorCode.UNAUTHORIZED, "이메일 존제하지 않습니다");
        }
        UsersEntity user = userService.findByEmailWithThrow(userEmail);
        UserCheckResponse responseData = userConvertor.entityToCheckResponse(user);
        return API.OK(responseData);
    }
    
    // 암호 인증 로직
    public API<String> getUserPasswordVerify(String password) {
        // 사용자가 입력한 암호의 누락 여부확인
        if(password == null)    {
            throw new ApiException(ErrorCode.BAD_REQUEST, "비밀번호가 누락되었습니다.");
        }
        
        // 현재 세션 정보 확인 및 추출 후 소셜 여부 확인
        UsersEntity user = userService.findByIdWithThrow(AuthUtil.getUserId());
        if(!user.getUserSocial().getSocialCode().equals("LOCAL"))   {
            throw new ApiException(ErrorCode.BAD_REQUEST, "레헬른 계정만 요청 가능합니다.");
        }
        
        // 암호화 모듈 로드 및 모듈을 이용한 암호일치 여부 확인
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        if(bCryptPasswordEncoder.matches(password, user.getUserPassword())) {
            return API.OK("인증에 성공하였습니다!");
        } else {
            throw new ApiException(ErrorCode.BAD_REQUEST, "비밀번호가 일치하지 않습니다!");
        }
    }

    // 유저 소셜타입 조회
    public API<String> getUserSocial(String userId)  {
        UsersEntity user = userService.findByIdWithThrow(userId);
        if(user != null)    {
            return API.OK(user.getUserSocial().getSocialCode(), "소셜 정보를 확인하세요.");
        } else {
            throw new ApiException(ErrorCode.BAD_REQUEST, "잘못된 요청입니다.");
        }
    }
}
