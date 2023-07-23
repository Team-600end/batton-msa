package com.batton.projectservice.dto.issue;

import com.batton.projectservice.domain.Issue;
import com.batton.projectservice.dto.client.GetMemberResDTO;
import com.batton.projectservice.enums.IssueStatus;
import com.batton.projectservice.enums.IssueTag;
import lombok.Builder;
import lombok.Getter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Getter
public class GetIssueInfoResDTO {
    private Long id;
    private String issueTitle;
    private String issueContent;
    @Enumerated(EnumType.STRING)
    private IssueTag issueTag;
    @Enumerated(EnumType.STRING)
    private IssueStatus issueStatus;
    private String nickname;
    private String profileImage;

    @Builder
    public GetIssueInfoResDTO(Long id, String issueTitle, String issueContent, IssueTag issueTag, IssueStatus issueStatus, String nickname, String profileImage) {
        this.id = id;
        this.issueTitle = issueTitle;
        this.issueContent = issueContent;
        this.issueTag = issueTag;
        this.issueStatus = issueStatus;
        this.nickname = nickname;
        this.profileImage = profileImage;
    }

    public static GetIssueInfoResDTO toDTO(Issue issue, GetMemberResDTO getMemberResDTO) {
        return GetIssueInfoResDTO.builder()
                .id(issue.getId())
                .issueTitle(issue.getIssueTitle())
                .issueContent(issue.getIssueContent())
                .issueTag(issue.getIssueTag())
                .issueStatus(issue.getIssueStatus())
                .nickname(getMemberResDTO.getNickname())
                .profileImage(getMemberResDTO.getProfileImage())
                .build();
    }
}
