package com.batton.projectservice.dto;

import com.batton.projectservice.domain.Project;
import lombok.Builder;
import lombok.Getter;

@Getter
public class GetProjectListResDTO {
    private Long projectId;
    private String projectTitle;
    private String projectContent;
    private String projectImage;

    @Builder
    public GetProjectListResDTO(Long projectId, String projectTitle, String projectContent, String projectImage) {
        this.projectId = projectId;
        this.projectTitle = projectTitle;
        this.projectContent = projectContent;
        this.projectImage = projectImage;
    }

    public static GetProjectListResDTO toDTO(Project project) {
        return GetProjectListResDTO.builder()
                .projectId(project.getId())
                .projectTitle(project.getProjectTitle())
                .projectContent(project.getProjectContent())
                .projectImage(project.getProjectImage())
                .build();
    }
}
