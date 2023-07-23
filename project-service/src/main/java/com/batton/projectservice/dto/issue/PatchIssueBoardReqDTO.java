package com.batton.projectservice.dto.issue;

import com.batton.projectservice.domain.Belong;
import com.batton.projectservice.domain.Issue;
import com.batton.projectservice.domain.Project;
import com.batton.projectservice.enums.IssueStatus;
import com.batton.projectservice.enums.IssueTag;
import lombok.Builder;
import lombok.Getter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.ArrayList;
import java.util.List;

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
