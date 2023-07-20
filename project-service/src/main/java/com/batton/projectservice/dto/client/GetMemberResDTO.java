package com.batton.projectservice.dto.client;

import lombok.Builder;

public class GetMemberResDTO {

    private String nickname;

    private String profileImage;

    @Builder
    public GetMemberResDTO(String nickname, String profileImage) {
        this.nickname = nickname;
        this.profileImage = profileImage;
    }
}