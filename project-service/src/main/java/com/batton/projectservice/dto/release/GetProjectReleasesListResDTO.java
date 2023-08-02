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
    private LocalDateTime createdAt;
    private List<Long> issueList;

    @Builder
    public GetProjectReleasesListResDTO(String versionChanged, int versionMajor, int versionMinor, int versionPatch, LocalDateTime createdAt, List<Long> issueList) {
        this.versionChanged = versionChanged;
        this.versionMajor = versionMajor;
        this.versionMinor = versionMinor;
        this.versionPatch = versionPatch;
        this.createdAt = createdAt;
        this.issueList = issueList;
    }

    public static GetProjectReleasesListResDTO toDTO(String versionChanged, int versionMajor, int versionMinor, int versionPatch, LocalDateTime createdAt, List<Long> issueList) {
        return GetProjectReleasesListResDTO.builder()
                .versionChanged(versionChanged)
                .versionMajor(versionMajor)
                .versionMinor(versionMinor)
                .versionPatch(versionPatch)
                .createdAt(createdAt)
                .issueList(issueList)
                .build();
    }
}
