package com.batton.projectservice.dto.issue;

import com.batton.projectservice.domain.Belong;
import com.batton.projectservice.domain.Issue;
import com.batton.projectservice.domain.Project;
import com.batton.projectservice.enums.IssueStatus;
import com.batton.projectservice.enums.IssueTag;
import lombok.Builder;
import lombok.Getter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Getter
public class PostIssueReqDTO {
    private Long projectId;
    private Long belongId;
    private String issueTitle;
    @Enumerated(EnumType.STRING)
    private IssueTag issueTag;

    @Builder
    public PostIssueReqDTO(Long projectId, Long belongId, String issueTitle, IssueTag issueTag) {
        this.projectId = projectId;
        this.belongId = belongId;
        this.issueTitle = issueTitle;
        this.issueTag = issueTag;
    }

    public static Issue toEntity(Project project, Belong belong, PostIssueReqDTO dto, IssueStatus issueStatus, int issueSeq) {
        return Issue.builder()
                .project(project)
                .belong(belong)
                .issueTitle(dto.issueTitle)
                .issueTag(dto.issueTag)
                .issueStatus(issueStatus)
                .issueSeq(issueSeq)
                .build();
    }
}
