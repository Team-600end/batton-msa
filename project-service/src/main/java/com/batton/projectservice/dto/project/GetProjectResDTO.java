package com.batton.projectservice.dto.project;

import com.batton.projectservice.domain.Project;
import lombok.Builder;
import lombok.Getter;

@Getter
public class GetProjectResDTO {
    private Long projectId;
    private String projectTitle;
    private String projectImage;
    private String projectKey;

    @Builder
    public GetProjectResDTO(Long projectId, String projectTitle, String projectImage, String projectKey) {
        this.projectId = projectId;
        this.projectTitle = projectTitle;
        this.projectImage = projectImage;
        this.projectKey = projectKey;
    }

    public static GetProjectResDTO toDTO(Project project) {
        return GetProjectResDTO.builder()
                .projectId(project.getId())
                .projectTitle(project.getProjectTitle())
                .projectImage(project.getProjectImage())
                .projectKey(project.getProjectKey())
                .build();
    }
}
