package com.batton.memberservice.dto;

import com.batton.memberservice.enums.Status;
import lombok.Builder;
import lombok.Getter;

@Getter
public class GetMemberResDTO {
    private Long memberId;
    private String nickname;
    private Status status;
    private String profileImage;
    private String email;

    @Builder
    public GetMemberResDTO(Long memberId, String nickname, Status status, String profileImage, String email) {
        this.memberId = memberId;
        this.nickname = nickname;
        this.status = status;
        this.profileImage = profileImage;
        this.email = email;
    }

}
