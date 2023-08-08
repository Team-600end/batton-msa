package com.batton.projectservice.dto.issue;

import com.batton.projectservice.domain.Issue;
import com.batton.projectservice.dto.client.GetMemberResDTO;
import com.batton.projectservice.dto.comment.GetCommentResDTO;
import com.batton.projectservice.enums.IssueTag;
import lombok.Builder;
import lombok.Getter;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.ArrayList;
import java.util.List;

@Getter
public class GetIssueReportResDTO {
    private int issueKey;
    private String issueTitle;
    @Enumerated(EnumType.STRING)
    private IssueTag issueTag;
    private String updatedDate;
    private String nickname;
    private String profileImage;
    private String reportContent;
    private List<GetCommentResDTO> commentList = new ArrayList<>();

    @Builder
    public GetIssueReportResDTO(int issueKey, String issueTitle, IssueTag issueTag, String updatedDate, String nickname, String profileImage, String reportContent, List<GetCommentResDTO> commentList) {
        this.issueKey = issueKey;
        this.issueTitle = issueTitle;
        this.issueTag = issueTag;
        this.updatedDate = updatedDate;
        this.nickname = nickname;
        this.profileImage = profileImage;
        this.reportContent = reportContent;
        this.commentList = commentList;
    }

    public static GetIssueReportResDTO toDTO(Issue issue, String updatedDate, GetMemberResDTO getMemberInfoResDTO, String reportContent, List<GetCommentResDTO> commentList) {
        return GetIssueReportResDTO.builder()
                .issueKey(issue.getIssueKey())
                .issueTitle(issue.getIssueTitle())
                .issueTag(issue.getIssueTag())
                .updatedDate(updatedDate)
                .nickname(getMemberInfoResDTO.getNickname())
                .profileImage(getMemberInfoResDTO.getProfileImage())
                .reportContent(reportContent)
                .commentList(commentList)
                .build();
    }

}
