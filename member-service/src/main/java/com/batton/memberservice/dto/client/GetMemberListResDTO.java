package com.batton.memberservice.dto.client;

import com.batton.memberservice.domain.Member;
import com.batton.memberservice.dto.GetMemberInfoResDTO;
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

    public static GetMemberListResDTO toDTO(Member member) {
        return GetMemberListResDTO.builder()
                .nickname(member.getNickname())
                .profileImage(member.getProfileImage())
                .email(member.getEmail())
                .build();
    }
}
