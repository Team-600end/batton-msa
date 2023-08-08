package com.batton.projectservice.dto.comment;

import com.batton.projectservice.domain.Comment;
import com.batton.projectservice.dto.client.GetMemberResDTO;
import com.batton.projectservice.enums.CommentType;
import lombok.Builder;
import lombok.Getter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Getter
public class GetCommentResDTO {
    private String commentContent;
    @Enumerated(EnumType.STRING)
    private CommentType commentType;
    private String createdDate;
    private String nickname;
    private String profileImage;

    @Builder
    public GetCommentResDTO(String commentContent, CommentType commentType, String createdDate, String nickname, String profileImage) {
        this.commentContent = commentContent;
        this.commentType = commentType;
        this.createdDate = createdDate;
        this.nickname = nickname;
        this.profileImage = profileImage;
    }

    public static GetCommentResDTO toDTO(Comment comment, GetMemberResDTO getMemberResDTO, String createdDate) {
        return GetCommentResDTO.builder()
                .commentContent(comment.getCommentContent())
                .commentType(comment.getCommentType())
                .createdDate(createdDate)
                .nickname(getMemberResDTO.getNickname())
                .profileImage(getMemberResDTO.getProfileImage())
                .build();
    }
}
