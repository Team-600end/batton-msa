package com.batton.projectservice.dto.project;

import com.batton.projectservice.domain.Belong;
import com.batton.projectservice.domain.Project;
import com.batton.projectservice.enums.GradeType;
import com.batton.projectservice.enums.Status;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@NoArgsConstructor
@Getter
public class ProjectTeamReqDTO {
    private Long memberId;
    private String nickname;
    @Enumerated(EnumType.STRING)
    private GradeType gradeType;
    @Enumerated(EnumType.STRING)
    private Status status;

    @Builder
    public ProjectTeamReqDTO(Long memberId, String nickname, GradeType gradeType, Status status) {
        this.memberId = memberId;
        this.nickname = nickname;
        this.gradeType = gradeType;
        this.status = status;
    }

    public static Belong toEntity(Project project, ProjectTeamReqDTO dto) {
        return Belong.builder()
                .project(project)
                .memberId(dto.memberId)
                .nickname(dto.nickname)
                .grade(dto.gradeType)
                .status(dto.status)
                .build();
    }
}
