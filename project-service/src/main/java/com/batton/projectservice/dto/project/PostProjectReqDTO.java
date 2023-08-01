package com.batton.projectservice.dto.project;

import com.batton.projectservice.domain.Project;
import lombok.Builder;
import lombok.Getter;
import java.util.List;

@Getter
public class PostProjectReqDTO {
    private String projectTitle;
    private String projectKey;
    private String projectContent;
    private String projectImage;
    private List<ProjectTeamReqDTO> projectMemberList;

    @Builder
    public PostProjectReqDTO(String projectTitle, String projectKey, String projectContent, String projectImage, List<ProjectTeamReqDTO> projectMemberList) {
        this.projectTitle = projectTitle;
        this.projectKey = projectKey;
        this.projectContent = projectContent;
        this.projectImage = projectImage;
        this.projectMemberList = projectMemberList;
    }

    public static Project toEntity(PostProjectReqDTO postProjectReqDTO) {
        return Project.builder()
                .projectTitle(postProjectReqDTO.getProjectTitle())
                .projectContent(postProjectReqDTO.getProjectContent())
                .projectImage(postProjectReqDTO.getProjectImage())
                .projectKey(postProjectReqDTO.getProjectKey())
                .build();
    }
}
