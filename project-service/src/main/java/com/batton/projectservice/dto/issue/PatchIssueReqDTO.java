package com.batton.projectservice.dto.issue;

import com.batton.projectservice.enums.IssueStatus;
import com.batton.projectservice.enums.IssueTag;
import lombok.Builder;
import lombok.Getter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Getter
public class PatchIssueReqDTO {
    private Long projectId;
    private String issueTitle;
    private String issueContent;
    @Enumerated(EnumType.STRING)
    private IssueTag issueTag;
    private Long belongId;

    @Builder
    public PatchIssueReqDTO(Long projectId, String issueTitle, String issueContent, IssueTag issueTag, Long belongId) {
        this.projectId =projectId;
        this.issueTitle = issueTitle;
        this.issueContent = issueContent;
        this.issueTag = issueTag;
        this.belongId = belongId;
    }
}
