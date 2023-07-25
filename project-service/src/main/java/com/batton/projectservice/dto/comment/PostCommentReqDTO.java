package com.batton.projectservice.dto.comment;

import com.batton.projectservice.domain.Belong;
import com.batton.projectservice.domain.Comment;
import com.batton.projectservice.domain.Issue;
import com.batton.projectservice.dto.project.PostProjectReqDTO;
import lombok.Builder;
import lombok.Getter;

import java.util.stream.DoubleStream;

@Getter
public class PostCommentReqDTO {
    private String commentContent;
    private Long belongId;
    private Long issueId;

    @Builder
    public PostCommentReqDTO(String commentContent, Long belongId, Long issueId) {
        this.commentContent = commentContent;
        this.belongId = belongId;
        this.issueId = issueId;
    }

    public static Comment toEntity(PostCommentReqDTO postCommentReqDTO, Belong belong, Issue issue) {
        return Comment.builder()
                .commentContent(postCommentReqDTO.commentContent)
                .belong(belong)
                .issue(issue)
                .build();
    }
}
