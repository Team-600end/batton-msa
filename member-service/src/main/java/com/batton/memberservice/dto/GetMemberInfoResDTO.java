package com.batton.memberservice.dto;

import com.batton.memberservice.domain.Member;
import com.batton.memberservice.enums.Status;
import lombok.Builder;
import lombok.Getter;

@Getter
public class GetMemberInfoResDTO {
    private Long memberId;
    private String nickname;
    private Status status;
    private String profileImage;
    private String email;

    @Builder
    public GetMemberInfoResDTO(Long memberId, String nickname, Status status, String profileImage, String email) {
        this.memberId = memberId;
        this.nickname = nickname;
        this.status = status;
        this.profileImage = profileImage;
        this.email = email;
    }

    public static GetMemberInfoResDTO toDTO(Member member) {
        return GetMemberInfoResDTO.builder()
                .memberId(member.getId())
                .nickname(member.getNickname())
                .status(member.getStatus())
                .profileImage(member.getProfileImage())
                .email(member.getEmail())
                .build();
    }
}
