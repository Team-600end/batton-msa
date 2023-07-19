package com.batton.memberservice.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SignupMemberReqDTO {
    private String email;
    private String nickname;
    private String password;
    // 비밀번호 암호화
//    private String encryptedPwd;
}
