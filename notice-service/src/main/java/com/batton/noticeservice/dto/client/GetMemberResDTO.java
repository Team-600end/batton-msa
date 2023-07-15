package com.batton.noticeservice.dto.client;

import lombok.Builder;
import lombok.Getter;

@Getter
public class GetMemberResDTO {
    private String nickname;

    private String profileImage;

    @Builder
    public GetMemberResDTO(String nickname, String profileImage) {
        this.nickname = nickname;
        this.profileImage = profileImage;
    }
}
