package com.batton.projectservice.dto.release;

import com.batton.projectservice.domain.Issue;
import com.batton.projectservice.enums.IssueTag;
import lombok.Builder;
import lombok.Getter;

@Getter
public class GetReleasesIssueResDTO {
    private IssueTag issueTag;
    private String issueTitle;

    @Builder
    public GetReleasesIssueResDTO(IssueTag issueTag, String issueTitle) {
        this.issueTag = issueTag;
        this.issueTitle = issueTitle;
    }

    public static GetReleasesIssueResDTO toDTO(Issue issue) {
        return GetReleasesIssueResDTO.builder()
                .issueTag(issue.getIssueTag())
                .issueTitle(issue.getIssueTitle())
                .build();
    }
}
