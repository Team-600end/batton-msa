package com.batton.projectservice.dto.release;

import com.batton.projectservice.enums.PublishState;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class PatchReleasesReqDTO {
    private int versionMajor;
    private int versionMinor;
    private int versionPatch;
    private String releaseContent;
    private List<Long> issueList;
    private PublishState publishState;

    @Builder
    public PatchReleasesReqDTO(int versionMajor, int versionMinor, int versionPatch, String releaseContent, List<Long> issueList) {
        this.versionMajor = versionMajor;
        this.versionMinor = versionMinor;
        this.versionPatch = versionPatch;
        this.releaseContent = releaseContent;
        this.issueList = issueList;
    }
}
