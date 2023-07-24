package com.batton.projectservice.dto.issue;

import com.batton.projectservice.domain.Issue;
import com.batton.projectservice.domain.Report;
import lombok.Builder;
import lombok.Getter;

@Getter
public class PatchIssueReportReqDTO {
    private Long issueId;
    private String reportContent;

    @Builder
    public PatchIssueReportReqDTO(Long issueId, String reportContent) {
        this.issueId = issueId;
        this.reportContent = reportContent;
    }
}
