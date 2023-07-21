package com.batton.projectservice;

import com.batton.projectservice.dto.PostProjectReqDTO;
import com.batton.projectservice.dto.ProjectTeamReqDTO;
import com.batton.projectservice.enums.GradeType;
import com.batton.projectservice.enums.Status;
import com.batton.projectservice.repository.BelongRepository;
import com.batton.projectservice.repository.ProjectRepository;
import com.batton.projectservice.service.ProjectService;
import com.batton.projectservice.domain.Project;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.OngoingStubbing;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class ProjectServiceTests {

    @Mock
    ProjectRepository projectRepository;
    @Mock
    BelongRepository belongRepository;
    @InjectMocks
    private ProjectService projectService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("프로젝트 생성")
    public void testAddProject() {
        // Given
        Long memberId = 1L;

        List<ProjectTeamReqDTO> teamMemberList = new ArrayList<>();
        ProjectTeamReqDTO teamMember1 = ProjectTeamReqDTO.builder()
                .memberId(1L)
                .nickname("닉네임1")
                .status(Status.ENABLED)
                .gradeType(GradeType.MANAGER)
                .build();

        teamMemberList.add(teamMember1);

        ProjectTeamReqDTO teamMember2 = ProjectTeamReqDTO.builder()
                .memberId(2L)
                .nickname("닉네임2")
                .status(Status.ENABLED)
                .gradeType(GradeType.MEMBER)
                .build();
        teamMemberList.add(teamMember2);

        PostProjectReqDTO postProjectReqDTO = PostProjectReqDTO.builder()

                .projectTitle("프로젝트 제목")
                .projectContent("프로젝트 내용")
                .projectImage("프로젝트 이미지")
                .teamMemberList(teamMemberList)
                .build();

        Project project = Project.builder()
                .projectTitle("프로젝트 제목")
                .projectContent("프로젝트 내용")
                .projectImage("프로젝트 이미지")
                .build();

        given(projectRepository.save(any())).willReturn(project);
//        given(projectRepository.save(any())).willReturn(null);
//        given(projectRepository.save(any())).willAnswer(invocation -> {
//            Project savedProject = invocation.getArgument(0);
//            savedProject.setId(1L);  // 예시로 ID를 1L로 설정
//            return savedProject;
//        });
//        given(projectRepository.save(any())).willReturn(new Project());


        projectService.addProject(memberId, postProjectReqDTO);
        verify(projectRepository).save(any());

//        when(projectRepository.save(any())).thenReturn(savedProject);
        //OngoingStubbing<Object> objectOngoingStubbing = when(belongRepository.save(any())).thenReturn(new Belong());
//        when(belongRepository.save(any())).thenReturn(new Belong());

//        // When
//        Long newProjectId = projectService.addProject(memberId, postProjectReqDTO);
//
//        // Then
//        verify(projectRepository, times(1)).save(any());
//        verify(belongRepository, times(2)).save(any());
//
//        // 응답 값에 프로젝트 ID가 포함되어 있는지 검증
//        assertEquals(1L, newProjectId);
    }
}





