package com.batton.projectservice;

import com.batton.projectservice.domain.Belong;
import com.batton.projectservice.dto.project.PostProjectReqDTO;
import com.batton.projectservice.dto.project.PostProjectResDTO;
import com.batton.projectservice.dto.project.ProjectTeamReqDTO;
import com.batton.projectservice.enums.GradeType;
import com.batton.projectservice.enums.Status;
import com.batton.projectservice.repository.BelongRepository;
import com.batton.projectservice.repository.ProjectRepository;
import com.batton.projectservice.service.ProjectService;
import com.batton.projectservice.domain.Project;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

//@ExtendWith(MockitoExtension.class)
//public class ProjectServiceTest {
//    @Mock
//    private ProjectRepository projectRepository;
//    @Mock
//    private BelongRepository belongRepository;
//    @InjectMocks
//    private ProjectService projectService;
//
//    @Test
//    public void testPostProjectSuccess() {
//        // Arrange
//        Long memberId = 1L;
//        List<ProjectTeamReqDTO> projectTeamReqDTOS = new ArrayList<>();
//        ProjectTeamReqDTO projectTeamReqDTO = new ProjectTeamReqDTO(2L, "die", GradeType.LEADER);
//        projectTeamReqDTOS.add(projectTeamReqDTO);
//        PostProjectReqDTO postProjectReqDTO = new PostProjectReqDTO("project1","kea","hahaha","qwer","live", projectTeamReqDTOS);
//        // ... postProjectReqDTO에 필요한 값 설정
//
//        when(projectService.postProjectMember(anyLong(), anyLong(), anyList())).thenReturn("프로젝트 팀원 추가 성공");
//        when(belongRepository.save(any(Belong.class))).thenReturn(belong);
//
//        // Act
//        PostProjectResDTO result = projectService.postProject(memberId, postProjectReqDTO);
//
//        // Assert
//        assertNotNull(result);
//        // ... 결과 값에 대한 추가적인 검증
//        verify(projectRepository, times(1)).save(any(Project.class));
//        verify(belongRepository, times(1)).save(any(Belong.class));
//        verify(projectService, times(1)).postProjectMember(anyLong(), anyLong(), anyList());
//    }
//
//    // ...
//}