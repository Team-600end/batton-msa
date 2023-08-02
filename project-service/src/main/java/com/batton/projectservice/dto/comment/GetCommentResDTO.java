package com.batton.projectservice.dto.comment;

import com.batton.projectservice.domain.Comment;
import com.batton.projectservice.dto.client.GetMemberResDTO;
import lombok.Builder;
import lombok.Getter;

@Getter
public class GetCommentResDTO {
    private String commentContent;
    private String createdDate;
    private String nickname;
    private String profileImage;

    @Builder
    public GetCommentResDTO(String commentContent, String createdDate, String nickname, String profileImage) {
        this.commentContent = commentContent;
        this.createdDate = createdDate;
        this.nickname = nickname;
        this.profileImage = profileImage;
    }

    public static GetCommentResDTO toDTO(Comment comment, GetMemberResDTO getMemberResDTO, String createdDate) {
        return GetCommentResDTO.builder()
                .commentContent(comment.getCommentContent())
                .createdDate(createdDate)
                .nickname(getMemberResDTO.getNickname())
                .profileImage(getMemberResDTO.getProfileImage())
                .build();
    }
}
