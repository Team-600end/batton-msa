package com.batton.projectservice.dto;

import com.batton.projectservice.domain.Issue;
import com.batton.projectservice.domain.Report;
import lombok.Builder;
import lombok.Getter;

@Getter
public class PostIssueReportReqDTO {
    private Long issueId;
    private String reportContent;

    @Builder
    public PostIssueReportReqDTO(Long issueId, String reportContent) {
        this.issueId = issueId;
        this.reportContent = reportContent;
    }

    public static Report toEntity(PostIssueReportReqDTO postIssueReportReqDTO, Issue issue) {
        return Report.builder()
                .issue(issue)
                .reportContent(postIssueReportReqDTO.reportContent)
                .build();
    }
}
