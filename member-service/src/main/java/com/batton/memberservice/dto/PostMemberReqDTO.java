package com.batton.memberservice.dto;

import com.batton.memberservice.domain.Member;
import com.batton.memberservice.enums.Authority;
import com.batton.memberservice.enums.Status;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PostMemberReqDTO {
    private String email;
    private String nickname;
    private String password;
    private String checkPassword;

    public static Member toEntity(PostMemberReqDTO postMemberReqDTO, String password, Authority authority, Status status) {
        return Member.builder()
                .email(postMemberReqDTO.email)
                .nickname(postMemberReqDTO.nickname)
                .password(password)
                .authority(authority)
                .status(status)
                .build();
    }
}
