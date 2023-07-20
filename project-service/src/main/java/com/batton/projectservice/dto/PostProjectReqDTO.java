package com.batton.projectservice.dto;

import com.batton.projectservice.domain.Project;
import lombok.Builder;
import lombok.Getter;
import java.util.List;

@Getter
public class PostProjectReqDTO {
    private String projectTitle;
    private String projectContent;
    private String projectImage;
    private List<ProjectTeamReqDTO> teamMemberList;

    @Builder
    public PostProjectReqDTO(String projectTitle, String projectContent, String projectImage, List<ProjectTeamReqDTO> teamMemberList) {
        this.projectTitle = projectTitle;
        this.projectContent = projectContent;
        this.projectImage = projectImage;
        this.teamMemberList = teamMemberList;
    }

    public static Project toEntity(PostProjectReqDTO dto, String projectKey) {
        return Project.builder()
                .projectTitle(dto.projectTitle)
                .projectContent(dto.projectContent)
                .projectImage(dto.projectImage)
                .projectKey(projectKey)
                .build();
    }
}
