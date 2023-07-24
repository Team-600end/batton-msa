package com.batton.memberservice.dto.client;

import com.batton.memberservice.domain.Member;
import com.batton.memberservice.dto.GetMemberInfoResDTO;
import com.batton.memberservice.enums.Status;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GetMemberResDTO {
    private String nickname;
    private String profileImage;

    @Builder
    public GetMemberResDTO(String nickname, String profileImage) {
        this.nickname = nickname;
        this.profileImage = profileImage;
    }

    public static GetMemberResDTO toDTO(Member member) {
        return GetMemberResDTO.builder()
                .nickname(member.getNickname())
                .profileImage(member.getProfileImage())
                .build();
    }
}

