package com.batton.projectservice.service;

import com.batton.projectservice.client.MemberServiceFeignClient;
import com.batton.projectservice.common.BaseException;
import com.batton.projectservice.domain.Belong;
import com.batton.projectservice.domain.Project;
import com.batton.projectservice.dto.client.GetMemberResDTO;
import com.batton.projectservice.dto.project.*;
import com.batton.projectservice.enums.GradeType;
import com.batton.projectservice.enums.Status;
import com.batton.projectservice.repository.BelongRepository;
import com.batton.projectservice.repository.ProjectRepository;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;
import java.lang.reflect.Member;
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
    private final MemberServiceFeignClient memberServiceFeignClient;


    /**
     * 프로젝트 생성 API
     * */
    @Transactional
    public PostProjectResDTO postProject(Long memberId, PostProjectReqDTO postProjectReqDTO) {
        Project project = postProjectReqDTO.toEntity(postProjectReqDTO);
        Long newProjectId = projectRepository.save(project).getId();

        // 프로젝트 생성한 사람일 경우 LEADER 권한 부여
        Belong leaderBelong = Belong.builder()
                .project(project)
                .memberId(memberId)
                .nickname(postProjectReqDTO.getNickname())
                .status(Status.ENABLED)
                .grade(GradeType.LEADER)
                .build();

        belongRepository.save(leaderBelong);

        //소속 테이블에 팀원들 추가하는 함수 불러오기
        postProjectMember(memberId, newProjectId, postProjectReqDTO.getProjectMemberList());
        PostProjectResDTO postProjectResDTO = PostProjectResDTO.toDto(newProjectId, postProjectReqDTO.getProjectKey());

        return postProjectResDTO;
    }

    /**
     * 프로젝트 고유키 중복 확인 API
     * */
    @Transactional
    public String getCheckKey(String projectKey) {
        List<Project> projectList = projectRepository.findAll();

        for (Project project : projectList) {
            if (project.getProjectKey().equals(projectKey)) {
                throw new BaseException(PROJECT_KEY_EXISTS);
            }
        }

        return "프로젝트 키가 유효합니다.";
    }

    /**
     * 프로젝트 팀원 추가 API
     * */
    @Transactional
    public String postProjectMember(Long memberId, Long projectId, List<ProjectTeamReqDTO> teamMemberList) {
        Optional<Project> project = projectRepository.findById(projectId);
        Optional<Belong> leaderBelong = belongRepository.findByProjectIdAndMemberId(projectId, memberId);

        // 프로젝트 존재 여부 확인
        if (project.isPresent()) {
            // 리더 권한 확인
            if (leaderBelong.isPresent() && leaderBelong.get().getGrade() == GradeType.LEADER) {
                // 팀원 추가
                for (ProjectTeamReqDTO projectTeamReqDTO : teamMemberList) {
                    Belong belong = ProjectTeamReqDTO.toEntity(project.get(), projectTeamReqDTO, Status.ENABLED);
                    belongRepository.save(belong);
                }
            } else {
                throw new BaseException(MEMBER_NO_AUTHORITY);
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
     * 프로젝트 상세 조회 API
     * */
    @Transactional
    public GetProjectInfoResDTO getProject(Long memberId, Long projectId) {
        Optional<Project> project = projectRepository.findById(projectId);
        Optional<Belong> belong = belongRepository.findByProjectIdAndMemberId(projectId, memberId);

        //프로젝트 존재 유뮤 확인
        if (project.isPresent()) {
            // 소속 유저 확인
            if (belong.isPresent() && belong.get().getStatus().equals(Status.ENABLED)) {
                GetProjectInfoResDTO getProjectInfoResDTO = GetProjectInfoResDTO.toDTO(project.get());

                return getProjectInfoResDTO;
            } else {
                throw new BaseException(BELONG_INVALID_ID);
            }
        } else {
            throw new BaseException(PROJECT_INVALID_ID);
        }
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

    /**
     * 프로젝트 목록 검색 조회 API
     */
    public List<GetProjectListResDTO> getProjectList(Long memberId, String keyword) {
        GetMemberResDTO getMemberResDTO = memberServiceFeignClient.getMember(memberId);
        List<GetProjectListResDTO> getProjectListResDTOSList = new ArrayList<>();
        List<Project> projectList = new ArrayList<>();

        if (StringUtils.isEmpty(keyword)) {
            // 전체 조회
            projectList = projectRepository.findAll();

        } else {
            // 특정 키워드 조회
            projectList = projectRepository.findByProjectTitleContaining(keyword);
        }

        for (Project project : projectList) {
            getProjectListResDTOSList.add(GetProjectListResDTO.toDTO(project));
        }

        return  getProjectListResDTOSList;
    }
}

