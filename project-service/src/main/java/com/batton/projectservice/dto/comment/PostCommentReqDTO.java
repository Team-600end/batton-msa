package com.batton.projectservice.dto.comment;

import com.batton.projectservice.domain.Belong;
import com.batton.projectservice.domain.Comment;
import com.batton.projectservice.domain.Report;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@NoArgsConstructor
public class PostCommentReqDTO {
    private String commentContent;

    @Builder
    public PostCommentReqDTO(String commentContent) {
        this.commentContent = commentContent;
    }

    public static Comment toEntity(PostCommentReqDTO postCommentReqDTO, Belong belong, Report report) {
        return Comment.builder()
                .commentContent(postCommentReqDTO.getCommentContent())
                .belong(belong)
                .report(report)
                .build();
    }
}
