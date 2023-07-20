package com.batton.projectservice.service;

import com.batton.projectservice.common.BaseException;
import com.batton.projectservice.domain.Belong;
import com.batton.projectservice.domain.Project;
import com.batton.projectservice.dto.PatchProjectReqDTO;
import com.batton.projectservice.dto.PostProjectReqDTO;
import com.batton.projectservice.dto.ProjectTeamReqDTO;
import com.batton.projectservice.enums.GradeType;
import com.batton.projectservice.repository.BelongRepository;
import com.batton.projectservice.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.batton.projectservice.common.BaseResponseStatus.*;

@RequiredArgsConstructor
@Service
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final BelongRepository belongRepository;

    /**
     * 프로젝트 생성 API
     * */
    @Transactional
    public Long addProject(Long memberId, PostProjectReqDTO postProjectReqDTO) {
        boolean isUnique = false;
        String projectKey = UUID.randomUUID().toString();

        //프로젝트 키가 중복되지 않을 때까지 반복
        while(!isUnique) {
            if(projectRepository.existsByProjectKey(projectKey))
                projectKey = UUID.randomUUID().toString();
            else
                isUnique = true;
        }
        Project project = postProjectReqDTO.toEntity(postProjectReqDTO, projectKey);
        Long newProjectId = projectRepository.save(project).getId();

        //소속 테이블에 팀원들 추가하는 함수 불러오기
        addTeamMember(memberId, newProjectId, postProjectReqDTO.getTeamMemberList());

        return newProjectId;
    }

    /**
     * 프로젝트 생성 API - 팀원 추가
     * */
    @Transactional
    public String addTeamMember(Long memberId, Long projectId, List<ProjectTeamReqDTO> teamMemberList) {
        Optional<Project> newProject = projectRepository.findById(projectId);

        if (newProject.isPresent()) {
            Project project = newProject.get();

            for (ProjectTeamReqDTO projectTeamReqDTO : teamMemberList) {
                //프로젝트 생성한 사람일 경우 MANAGER 권한 부여
                if (projectTeamReqDTO.getMemberId() == memberId) {
                    Belong belong = Belong.builder()
                            .project(project)
                            .memberId(projectTeamReqDTO.getMemberId())
                            .nickname(projectTeamReqDTO.getNickname())
                            .status(projectTeamReqDTO.getStatus())
                            .grade(GradeType.LEADER)
                            .build();

                    belongRepository.save(belong);
                } else {
                    //프로젝트 생성한 사람이 아닐 경우 다른 권한 부여
                    Belong belong = ProjectTeamReqDTO.toEntity(project, projectTeamReqDTO);

                    belongRepository.save(belong);
                }
            }
        } else {
            throw new BaseException(PROJECT_NOT_FOUND);
        }

        return "프로젝트 팀원 추가 성공";
    }

    /**
     * 프로젝트 수정 API
     * */
    @Transactional
    public String modifyProject(Long projectId, Long memberId, PatchProjectReqDTO patchProjectReqDTO) {
        Optional<Belong> belong = belongRepository.findByProjectIdAndMemberId(projectId, memberId);

        if (belong.isPresent()) {
            if (belong.get().getGrade() == GradeType.MEMBER) {
                throw new BaseException(USER_NO_AUTHORITY);
            }
            Optional<Project> project = projectRepository.findById(projectId);

            if (project.isPresent()) {
                project.get().update(patchProjectReqDTO.getProjectTitle(), patchProjectReqDTO.getProjectContent(), patchProjectReqDTO.getProjectImage());
            } else {
                throw new BaseException(PROJECT_NOT_FOUND);
            }
        } else {
            throw new BaseException(USER_NOT_FOUND);
        }

        return "프로젝트 수정 성공";
    }

    /**
     * 프로젝트 삭제 API
     * */
    @Transactional
    public String removeProject(Long memberId, Long projectId) {
        Optional<Belong> belong = belongRepository.findByProjectIdAndMemberId(projectId, memberId);

        if (belong.isPresent()) {
            if (belong.get().getGrade() == GradeType.MEMBER) {
                throw new BaseException(USER_NO_AUTHORITY);
            } else {
                projectRepository.deleteById(projectId);
            }
        } else {
            throw new BaseException(PROJECT_NOT_FOUND);
        }

        return "프로젝트 삭제 성공";
    }
}
