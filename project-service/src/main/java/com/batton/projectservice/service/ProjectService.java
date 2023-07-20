package com.batton.projectservice.service;

import com.batton.projectservice.common.BaseException;
import com.batton.projectservice.domain.Belong;
import com.batton.projectservice.domain.Project;
import com.batton.projectservice.dto.PostProjectReqDTO;
import com.batton.projectservice.dto.ProjectTeamReqDTO;
import com.batton.projectservice.enums.GradeType;
import com.batton.projectservice.enums.Status;
import com.batton.projectservice.repository.BelongRepository;
import com.batton.projectservice.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

import static com.batton.projectservice.common.BaseResponseStatus.NOT_FOUND;

@RequiredArgsConstructor
@Service
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final BelongRepository belongRepository;

    @Transactional
    public Long addProject(Long memberId, PostProjectReqDTO postProjectReqDTO) {
        boolean isUnique = false;
        String projectKey = UUID.randomUUID().toString();

        while(!isUnique) {
            if(projectRepository.existsByProjectKey(projectKey))
                projectKey = UUID.randomUUID().toString();
            else
                isUnique = true;
        }

        Project project = Project.builder()
                .projectTitle(postProjectReqDTO.getProjectTitle())
                .projectContent(postProjectReqDTO.getProjectContent())
                .projectImage(postProjectReqDTO.getProjectImage())
                .projectKey(projectKey)
                .build();

        Long newProjectId = projectRepository.save(project).getId();

        addTeamMember(memberId, newProjectId, postProjectReqDTO.getTeamMemberList());

        return newProjectId;
    }

    public String addTeamMember(Long memberId, Long projectId, List<ProjectTeamReqDTO> teamMemberList) {

        Project newProject = projectRepository.findById(projectId).get();
//        Project newProject = projectRepository.findById(projectId).orElseThrow(() -> new BaseException(NOT_FOUND));

        for (ProjectTeamReqDTO projectTeamReqDTO : teamMemberList) {
            if(projectTeamReqDTO.getMemberId() == memberId){
                Belong belong = Belong.builder()
                        .project(newProject)
                        .memberId(projectTeamReqDTO.getMemberId())
                        .nickname(projectTeamReqDTO.getNickname())
                        .status(Status.ENABLED)
                        .grade(GradeType.MANAGER)
                        .build();

                belongRepository.save(belong);
            } else {
                Belong belong = Belong.builder()
                        .project(newProject)
                        .memberId(projectTeamReqDTO.getMemberId())
                        .nickname(projectTeamReqDTO.getNickname())
                        .status(projectTeamReqDTO.getStatus())
                        .grade(projectTeamReqDTO.getGradeType())
                        .build();

                belongRepository.save(belong);
            }
        }

        return "프로젝트 팀원 추가 성공";
    }
}
