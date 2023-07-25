package com.batton.projectservice.dto.report;

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
