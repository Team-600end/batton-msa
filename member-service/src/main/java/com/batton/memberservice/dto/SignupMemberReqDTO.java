package com.batton.memberservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SignupMemberReqDTO {
    private String email;
    private String nickname;
    private String password;
    // 비밀번호 암호화
    private String encryptedPwd;
}
