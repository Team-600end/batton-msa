package com.batton.projectservice.dto.release;

import com.batton.projectservice.domain.Issue;
import com.batton.projectservice.enums.IssueTag;
import lombok.Builder;
import lombok.Getter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Getter
public class ReleaseIssueListResDTO {
    private Long issueId;
    private String issueTitle;
    @Enumerated(EnumType.STRING)
    private IssueTag issueTag;

    @Builder
    public ReleaseIssueListResDTO(Long issueId, String issueTitle, IssueTag issueTag) {
        this.issueId = issueId;
        this.issueTitle = issueTitle;
        this.issueTag = issueTag;
    }

    public static ReleaseIssueListResDTO toDTO(Long issueId, Issue issue) {
        return ReleaseIssueListResDTO.builder()
                .issueId(issueId)
                .issueTitle(issue.getIssueTitle())
                .issueTag(issue.getIssueTag())
                .build();
    }
}
