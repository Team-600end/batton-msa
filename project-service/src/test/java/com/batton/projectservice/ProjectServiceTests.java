//package com.batton.projectservice;
//
//import com.batton.projectservice.dto.PostProjectReqDTO;
//import com.batton.projectservice.dto.ProjectTeamReqDTO;
//import com.batton.projectservice.enums.GradeType;
//import com.batton.projectservice.enums.Status;
//import com.batton.projectservice.repository.BelongRepository;
//import com.batton.projectservice.repository.ProjectRepository;
//import com.batton.projectservice.service.BelongService;
//import com.batton.projectservice.service.ProjectService;
//import com.batton.projectservice.domain.Project;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.mockito.stubbing.OngoingStubbing;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//
//
//import java.util.ArrayList;
//import java.util.List;
//
//import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.BDDMockito.given;
//import static org.mockito.Mockito.*;
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//
//@SpringBootTest
//@AutoConfigureMockMvc
//@ExtendWith(MockitoExtension.class)
//public class ProjectServiceTests {
//
//    @Mock
//    ProjectRepository projectRepository;
//    @Mock
//    BelongRepository belongRepository;
//    @InjectMocks
//    private ProjectService projectService;
//    @InjectMocks
//    private BelongService belongService;
//
//    @BeforeEach
//    public void setup() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    @DisplayName("프로젝트 네비바 리스트 조회 서비스 테스트")
//    public void testNavbar() throws Exception {
//        // given
//        List<Project> projectList = new ArrayList<>();
//        Project project1 = Project.builder()
//                .id(1L)
//                .title("프로젝트1")
//                .projectKey("프로젝트키1")
//                .status(Status.ENABLED)
//                .build();
//        projectList.add(project1);
//
//        Project project2 = Project.builder()
//                .id(2L)
//                .title("프로젝트2")
//                .projectKey("프로젝트키2")
//                .status(Status.ENABLED)
//                .build();
//        projectList.add(project2);
//
//        given(projectRepository.findProjectsByMemberId(anyLong())).willReturn(projectList);
//
//        // when
//        List<Project> projects = projectService.findProjectsByMemberId(1L);
//
//        // then
//        verify(projectRepository, times(1)).findProjectsByMemberId(anyLong());
//        assertEquals(2, projects.size());
//        assertThat(projects.get(0).getTitle()).isEqualTo("프로젝트1");
//        assertThat(projects.get(1).getTitle()).isEqualTo("프로젝트2");
//    }
//
////    @Autowired
////    private MockMvc mockMvc; // mockMvc 생성
////
////    @Test
////    @DisplayName("프로젝트 생성")
////    public void testAddProject() throws Exception {
////
////        List<ProjectTeamReqDTO> teamMemberList = new ArrayList<>();
////        ProjectTeamReqDTO teamMember1 = ProjectTeamReqDTO.builder()
////                .memberId(1L)
////                .nickname("닉네임1")
////                .status(Status.ENABLED)
////                .gradeType(GradeType.MANAGER)
////                .build();
////
////        teamMemberList.add(teamMember1);
////
////        ProjectTeamReqDTO teamMember2 = ProjectTeamReqDTO.builder()
////                .memberId(2L)
////                .nickname("닉네임2")
////                .status(Status.ENABLED)
////                .gradeType(GradeType.MEMBER)
////                .build();
////        teamMemberList.add(teamMember2);
////
////        // given
////        PostProjectReqDTO postProjectReqDTO = new PostProjectReqDTO( "프로젝트제목", "프로젝트 내용", "프로젝트 이미지", teamMemberList);
////        when(projectRepository.save(any())).thenReturn(savedProject);
////        OngoingStubbing<Object> objectOngoingStubbing = when(belongRepository.save(any())).thenReturn(new Belong());
////        when(belongRepository.save(any())).thenReturn(new Belong());
////
////        // When
////        Long newProjectId = projectService.addProject(memberId, postProjectReqDTO);
////
////        // Then
////        verify(projectRepository, times(1)).save(any());
////        verify(belongRepository, times(2)).save(any());
////
////        // 응답 값에 프로젝트 ID가 포함되어 있는지 검증
////        assertEquals(1L, newProjectId);
////    }
////
////    @Test
////    @DisplayName("프로젝트 멤버 삭제 서비스 테스트")
////    void deleteBoardServiceTest() {
////        // given
////        Project project = new Project()
////        Belong belong = new Belong(1L, GradeType.LEADER, 2L, "테스트닉네임", Status.ENABLED, );
////        given(boardRepository.findById(anyLong())).willReturn(Optional.of(board));
////
////        // when
////        boardService.deleteBoard(1L);
////
////        // then
////        verify(boardRepository).deleteById(anyLong());
////    }
//}
//
///**
// * 프로젝트 생성 API
// * 프로젝트 멤버 추가 API
// * 프로젝트 멤버 삭제 API
// * test code 작성하는 법을 모르겠음,,,
// */
//
//
//
