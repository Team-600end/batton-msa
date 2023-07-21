package com.batton.memberservice.dto.client;

import lombok.Builder;
import lombok.Getter;

@Getter
public class GetMemberListResDTO {
    private String nickname;
    private String profileImage;
    private String email;

    @Builder
    public GetMemberListResDTO(String nickname, String profileImage, String email) {
        this.nickname = nickname;
        this.profileImage = profileImage;
        this.email = email;
    }
}
