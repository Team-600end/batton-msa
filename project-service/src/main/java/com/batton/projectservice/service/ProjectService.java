package com.batton.projectservice.service;

import com.batton.projectservice.common.BaseException;
import com.batton.projectservice.domain.Belong;
import com.batton.projectservice.domain.Project;
import com.batton.projectservice.dto.project.PatchProjectReqDTO;
import com.batton.projectservice.dto.project.PostProjectReqDTO;
import com.batton.projectservice.dto.project.ProjectTeamReqDTO;
import com.batton.projectservice.dto.project.GetProjectResDTO;
import com.batton.projectservice.enums.GradeType;
import com.batton.projectservice.enums.Status;
import com.batton.projectservice.repository.BelongRepository;
import com.batton.projectservice.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.ArrayList;
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
    public Long postProject(Long memberId, PostProjectReqDTO postProjectReqDTO) {
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
        postPojectMember(memberId, newProjectId, postProjectReqDTO.getTeamMemberList());

        return newProjectId;
    }

    /**
     * 프로젝트 팀원 추가 API
     * */
    @Transactional
    public String postPojectMember(Long memberId, Long projectId, List<ProjectTeamReqDTO> teamMemberList) {
        Optional<Project> project = projectRepository.findById(projectId);

        // 프로젝트 존재 여부 확인
        if (project.isPresent()) {
            for (ProjectTeamReqDTO projectTeamReqDTO : teamMemberList) {
                //프로젝트 생성한 사람일 경우 LEADER 권한 부여
                if (projectTeamReqDTO.getMemberId() == memberId) {
                    Belong belong = Belong.builder()
                            .project(project.get())
                            .memberId(projectTeamReqDTO.getMemberId())
                            .nickname(projectTeamReqDTO.getNickname())
                            .status(Status.ENABLED)
                            .grade(GradeType.LEADER)
                            .build();

                    belongRepository.save(belong);
                } else {
                    //프로젝트 생성한 사람이 아닐 경우 다른 권한 부여
                    Belong belong = ProjectTeamReqDTO.toEntity(project.get(), projectTeamReqDTO, Status.ENABLED);

                    belongRepository.save(belong);
                }
            }
        } else {
            throw new BaseException(PROJECT_INVALID_ID);
        }

        return "프로젝트 팀원 추가 성공";
    }

    /**
     * 프로젝트 수정 API
     * */
    @Transactional
    public String patchProject(Long projectId, Long memberId, PatchProjectReqDTO patchProjectReqDTO) {
        Optional<Belong> belong = belongRepository.findByProjectIdAndMemberId(projectId, memberId);

        // 소속 유저 확인
        if (belong.isPresent() && belong.get().getStatus().equals(Status.ENABLED))  {
            // 수정 권한 확인
            if (belong.get().getGrade() == GradeType.MEMBER) {
                throw new BaseException(MEMBER_NO_AUTHORITY);
            }
            Optional<Project> project = projectRepository.findById(projectId);

            // 프로젝트 존재 유무 확인
            if (project.isPresent()) {
                project.get().update(patchProjectReqDTO.getProjectTitle(), patchProjectReqDTO.getProjectContent(), patchProjectReqDTO.getProjectImage());
            } else {
                throw new BaseException(PROJECT_INVALID_ID);
            }
        } else {
            throw new BaseException(BELONG_INVALID_ID);
        }

        return "프로젝트 수정 성공";
    }

    /**
     * 프로젝트 삭제 API
     * */
    @Transactional
    public String deleteProject(Long memberId, Long projectId) {
        Optional<Belong> belong = belongRepository.findByProjectIdAndMemberId(projectId, memberId);

        // 소속 유저 확인
        if (belong.isPresent() && belong.get().getStatus().equals(Status.ENABLED)) {
            // 삭제 권한 확인
            if (belong.get().getGrade() == GradeType.MEMBER) {
                throw new BaseException(MEMBER_NO_AUTHORITY);
            } else {
                projectRepository.deleteById(projectId);
            }
        } else {
            throw new BaseException(PROJECT_INVALID_ID);
        }

        return "프로젝트 삭제 성공";
    }

    /**
     * 프로젝트 네비바 리스트 조회 API
     */
    public List<GetProjectResDTO> getProjectListForNavbar(Long memberId) {
        List<Belong> belongList = belongRepository.findByMemberId(memberId);

        if (!belongList.isEmpty()) {
            List<GetProjectResDTO> getProjectResDTOList = new ArrayList<>();

            for (Belong belong : belongList) {
                if (belong.getStatus().equals(Status.ENABLED)) {
                    getProjectResDTOList.add(GetProjectResDTO.toDTO(belong.getProject(), belong.getGrade()));
                }
            }

            return getProjectResDTOList;
        } else {
            throw new BaseException(PROJECT_NOT_EXISTS);
        }
    }
}

