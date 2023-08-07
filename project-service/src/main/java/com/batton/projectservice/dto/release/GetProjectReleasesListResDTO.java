package com.batton.projectservice.dto.release;

import com.batton.projectservice.domain.Issue;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class GetProjectReleasesListResDTO {
    private String versionChanged;
    private int versionMajor;
    private int versionMinor;
    private int versionPatch;
    private String createdDate;
//    private List<Long> issueList;
//    private List<Issue> issueList;
    private List<ReleaseIssueListResDTO> issueList;

    @Builder
    public GetProjectReleasesListResDTO(String versionChanged, int versionMajor, int versionMinor, int versionPatch, String createdDate, List<ReleaseIssueListResDTO> issueList) {
        this.versionChanged = versionChanged;
        this.versionMajor = versionMajor;
        this.versionMinor = versionMinor;
        this.versionPatch = versionPatch;
        this.createdDate = createdDate;
        this.issueList = issueList;
    }

    public static GetProjectReleasesListResDTO toDTO(String versionChanged, int versionMajor, int versionMinor, int versionPatch, String createdDate, List<ReleaseIssueListResDTO> issueList) {
        return GetProjectReleasesListResDTO.builder()
                .versionChanged(versionChanged)
                .versionMajor(versionMajor)
                .versionMinor(versionMinor)
                .versionPatch(versionPatch)
                .createdDate(createdDate)
                .issueList(issueList)
                .build();
    }
}