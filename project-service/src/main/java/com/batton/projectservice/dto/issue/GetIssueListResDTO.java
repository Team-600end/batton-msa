package com.batton.projectservice.dto.issue;

import com.batton.projectservice.domain.Issue;
import com.batton.projectservice.domain.Project;
import com.batton.projectservice.dto.GetProjectListResDTO;
import com.batton.projectservice.dto.client.GetMemberResDTO;
import com.batton.projectservice.enums.IssueTag;
import lombok.Builder;
import lombok.Getter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Getter
public class GetIssueListResDTO {
    private Long issueId;
    private String issueTitle;
    @Enumerated(EnumType.STRING)
    private IssueTag issueTag;
    private int issueSeq;
    private int issueKey;
    private String nickname;
    private String profileImage;

    @Builder
    public GetIssueListResDTO (Long issueId, String issueTitle, IssueTag issueTag, int issueSeq, int issueKey, String nickname, String profileImage) {
        this.issueId = issueId;
        this.issueTitle = issueTitle;
        this.issueTag = issueTag;
        this.issueSeq = issueSeq;
        this.issueKey = issueKey;
        this.nickname = nickname;
        this.profileImage = profileImage;
    }

    public static GetIssueListResDTO toDTO(Issue issue, GetMemberResDTO getMemberResDTO) {
        return GetIssueListResDTO.builder()
                .issueId(issue.getId())
                .issueTitle(issue.getIssueTitle())
                .issueTag(issue.getIssueTag())
                .issueSeq(issue.getIssueSeq())
                .issueKey(issue.getIssueKey())
                .nickname(getMemberResDTO.getNickname())
                .profileImage(getMemberResDTO.getProfileImage())
                .build();
    }

}
