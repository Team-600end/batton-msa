package com.batton.projectservice.dto.release;

import com.batton.projectservice.domain.Issue;
import com.batton.projectservice.enums.IssueTag;
import lombok.Builder;
import lombok.Getter;

@Getter
public class GetReleasesIssueReqDTO {
    private IssueTag issueTag;
    private String issueTitle;

    @Builder
    public GetReleasesIssueReqDTO(IssueTag issueTag, String issueTitle) {
        this.issueTag = issueTag;
        this.issueTitle = issueTitle;
    }

    public static GetReleasesIssueReqDTO toDTO(Issue issue) {
        return GetReleasesIssueReqDTO.builder()
                .issueTag(issue.getIssueTag())
                .issueTitle(issue.getIssueTitle())
                .build();
    }
}
