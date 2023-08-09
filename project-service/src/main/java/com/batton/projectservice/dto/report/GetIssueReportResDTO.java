package com.batton.projectservice.dto.report;

import com.batton.projectservice.domain.Report;
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
    private Long reportId;
    private int issueKey;
    private String issueTitle;
    @Enumerated(EnumType.STRING)
    private IssueTag issueTag;
    private String updatedDate;
    private String nickname;
    private String reportContent;
    private List<GetCommentResDTO> commentList = new ArrayList<>();

    @Builder
    public GetIssueReportResDTO(Long reportId, int issueKey, String issueTitle, IssueTag issueTag, String updatedDate, String nickname, String reportContent, List<GetCommentResDTO> commentList) {
        this.reportId = reportId;
        this.issueKey = issueKey;
        this.issueTitle = issueTitle;
        this.issueTag = issueTag;
        this.updatedDate = updatedDate;
        this.nickname = nickname;
        this.reportContent = reportContent;
        this.commentList = commentList;
    }

    public static GetIssueReportResDTO toDTO(Report report, String updatedDate, String nickname, List<GetCommentResDTO> commentList) {
        return GetIssueReportResDTO.builder()
                .reportId(report.getId())
                .issueKey(report.getIssue().getIssueKey())
                .issueTitle(report.getIssue().getIssueTitle())
                .issueTag(report.getIssue().getIssueTag())
                .updatedDate(updatedDate)
                .nickname(nickname)
                .reportContent(report.getReportContent())
                .commentList(commentList)
                .build();
    }

}
