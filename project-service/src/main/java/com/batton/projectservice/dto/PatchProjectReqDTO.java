package com.batton.projectservice.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class PatchProjectReqDTO {
    private String projectTitle;
    private String projectContent;
    private String projectImage;

    @Builder
    public PatchProjectReqDTO(String projectTitle, String projectContent, String projectImage) {
        this.projectTitle = projectTitle;
        this.projectContent = projectContent;
        this.projectImage = projectImage;
    }
}
