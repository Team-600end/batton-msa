package com.batton.projectservice.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
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

}
