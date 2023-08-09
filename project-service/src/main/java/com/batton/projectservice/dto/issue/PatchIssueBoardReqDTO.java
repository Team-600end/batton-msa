package com.batton.projectservice.dto.issue;

import com.batton.projectservice.enums.IssueCase;
import com.batton.projectservice.enums.IssueStatus;
import lombok.Builder;
import lombok.Getter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Getter
public class PatchIssueBoardReqDTO {
    private int seqNum;
    @Enumerated(EnumType.STRING)
    private IssueStatus afterStatus;
    @Enumerated(EnumType.STRING)
    private IssueCase issueCase;

    @Builder
    public PatchIssueBoardReqDTO(int seqNum, IssueStatus afterStatus, IssueCase issueCase) {
        this.seqNum = seqNum;
        this.afterStatus = afterStatus;
        this.issueCase = issueCase;
    }
}
