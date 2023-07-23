package com.batton.projectservice.dto.issue;

import com.batton.projectservice.domain.Issue;
import com.batton.projectservice.domain.Project;
import com.batton.projectservice.enums.IssueStatus;
import com.batton.projectservice.enums.IssueTag;
import lombok.Builder;
import lombok.Getter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Getter
public class GetMyIssueResDTO {
    private Long id;
    private String issueTitle;
    @Enumerated(EnumType.STRING)
    private IssueTag issueTag;
    @Enumerated(EnumType.STRING)
    private IssueStatus issueStatus;
    private String updatedDate;
    private String projectTitle;

    @Builder
    public GetMyIssueResDTO(Long id, String issueTitle, IssueTag issueTag, IssueStatus issueStatus, String updatedDate, String projectTitle) {
        this.id = id;
        this.issueTitle = issueTitle;
        this.issueTag = issueTag;
        this.issueStatus = issueStatus;
        this.updatedDate = updatedDate;
        this.projectTitle = projectTitle;
    }

    public static GetMyIssueResDTO toDTO(Issue issue, String updatedDate) {
        return GetMyIssueResDTO.builder()
                .id(issue.getId())
                .issueTitle(issue.getIssueTitle())
                .issueTag(issue.getIssueTag())
                .issueStatus(issue.getIssueStatus())
                .updatedDate(updatedDate)
                .projectTitle(issue.getProject().getProjectTitle())
                .build();
    }
}