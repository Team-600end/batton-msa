package com.batton.projectservice.dto.project;

import com.batton.projectservice.domain.Project;
import com.batton.projectservice.enums.GradeType;
import lombok.Builder;
import lombok.Getter;

@Getter
public class GetProjectInfoResDTO {
    private String projectTitle;
    private String projectContent;
    private String projectLogo;

    @Builder
    public GetProjectInfoResDTO(String projectTitle, String projectContent, String projectLogo) {
        this.projectTitle = projectTitle;
        this.projectContent = projectContent;
        this.projectLogo = projectLogo;
    }

    public static GetProjectInfoResDTO toDTO(Project project) {
        return GetProjectInfoResDTO.builder()
                .projectTitle(project.getProjectTitle())
                .projectContent(project.getProjectContent())
                .projectLogo(project.getProjectImage())
                .build();
    }
}
