package com.batton.projectservice.dto.issue;

import com.batton.projectservice.enums.IssueStatus;
import lombok.Builder;
import lombok.Getter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Getter
public class PatchIssueBoardReqDTO {
    private Long projectId;
    private int seqNum;
    @Enumerated(EnumType.STRING)
    private IssueStatus afterStatus;

    @Builder
    public PatchIssueBoardReqDTO(Long projectId, int seqNum, IssueStatus afterStatus) {
        this.projectId = projectId;
        this.seqNum = seqNum;
        this.afterStatus = afterStatus;
    }
}
