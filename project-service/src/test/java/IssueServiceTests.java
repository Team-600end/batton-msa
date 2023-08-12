import com.batton.projectservice.common.BaseException;
import com.batton.projectservice.domain.Belong;
import com.batton.projectservice.domain.Issue;
import com.batton.projectservice.domain.Project;
import com.batton.projectservice.dto.issue.*;
import com.batton.projectservice.enums.*;
import com.batton.projectservice.repository.BelongRepository;
import com.batton.projectservice.repository.IssueRepository;
import com.batton.projectservice.repository.ProjectRepository;
import com.batton.projectservice.repository.ReportRepository;
import com.batton.projectservice.service.IssueService;
import com.batton.projectservice.service.ProjectService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class IssueServiceTests {
    @InjectMocks
    private ProjectService projectService;
    @InjectMocks
    private IssueService issueService;
    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private BelongRepository belongRepository;

    @Mock
    private IssueRepository issueRepository;

    @Mock
    private ReportRepository reportRepository;

    @Test
    @DisplayName("이슈 생성 성공")
    public void testPostIssueSuccess() {
        // given
        Project project = new Project(1L,"project","test project", "image", "kea");
        Belong belong = new Belong(1L, GradeType.LEADER,1L,"harry", Status.ENABLED,project);
        List<Issue> issueList = new ArrayList<>();
        PostIssueReqDTO postIssueReqDTO = new PostIssueReqDTO(project.getId(),"issue","content", IssueTag.FIXED);
        Issue issue = new Issue(1L,"issue","content",IssueStatus.TODO,IssueTag.FIXED,3,2,project,belong,"null");

        when(projectRepository.findById(project.getId())).thenReturn(Optional.of(project));
        when(belongRepository.findByProjectIdAndMemberId(project.getId(), belong.getMemberId())).thenReturn(Optional.of(belong));
        when(issueRepository.findByIssueStatusOrderByIssueSeq(IssueStatus.TODO)).thenReturn(issueList);
        when(issueRepository.existsByProjectId(project.getId())).thenReturn(false);
        when(issueRepository.save(any(Issue.class))).thenReturn(issue);
        when(issueRepository.findById(issue.getId())).thenReturn(Optional.of(issue));

        // when
        Long issueId = issueService.postIssue(belong.getMemberId(), postIssueReqDTO);

        // then
        assertEquals(1L, issueId);
    }

    @Test
    @DisplayName("이슈 생성 시 프로젝트 예외 처리")
    public void testPostIssueInvalidProject() {
        // given
        Project project = new Project(1L,"project","test project", "image", "kea");
        Belong belong = new Belong(1L, GradeType.LEADER,1L,"harry", Status.ENABLED,project);
        PostIssueReqDTO postIssueReqDTO = new PostIssueReqDTO(project.getId(),"issue","content", IssueTag.FIXED);

        when(projectRepository.findById(project.getId())).thenReturn(Optional.empty());

        // when, then
        assertThrows(BaseException.class, () -> issueService.postIssue(belong.getMemberId(), postIssueReqDTO));
    }

    @Test
    @DisplayName("이슈 생성 시 소속 예외 처리")
    public void testPostIssueInvalidBelong() {
        // given
        Project project = new Project(1L,"project","test project", "image", "kea");
        Belong belong = new Belong(1L, GradeType.LEADER,1L,"harry", Status.ENABLED,project);
        PostIssueReqDTO postIssueReqDTO = new PostIssueReqDTO(project.getId(),"issue","content", IssueTag.FIXED);

        when(projectRepository.findById(project.getId())).thenReturn(Optional.of(project));
        when(belongRepository.findByProjectIdAndMemberId(project.getId(), belong.getMemberId())).thenReturn(Optional.empty());

        // when, then
        assertThrows(BaseException.class, () -> issueService.postIssue(belong.getMemberId(), postIssueReqDTO));
    }

    @Test
    @DisplayName("이슈 보드 상태 및 순서 변경 성공")
    public void testPatchIssueSuccess() {
        // given
        Project project = new Project(1L,"project","test project", "image", "kea");
        Belong belong = new Belong(1L, GradeType.LEADER,1L,"harry", Status.ENABLED,project);
        List<Issue> issueList = new ArrayList<>();
        Issue issue1 = new Issue(1L, "issue1","content",IssueStatus.TODO,IssueTag.CHANGED,1,2,project,belong,"null");
        Issue issue2 = new Issue(2L,"issue2","content",IssueStatus.PROGRESS,IssueTag.FIXED,1,1,project,belong,"null");
        issueList.add(issue2);
        List<Belong> belongList = new ArrayList<>();
        belongList.add(belong);
        PatchIssueBoardReqDTO patchIssueBoardReqDTO = new PatchIssueBoardReqDTO(0,IssueStatus.TODO,IssueStatus.PROGRESS, IssueCase.COMMON);

        when(issueRepository.findById(issue1.getId())).thenReturn(Optional.of(issue1));
        when(belongRepository.findByProjectIdAndMemberId(project.getId(), belong.getMemberId())).thenReturn(Optional.of(belong));
        when(issueRepository.findByIssueStatusOrderByIssueSeq(IssueStatus.PROGRESS)).thenReturn(issueList);
        when(belongRepository.findLeader(project.getId(), GradeType.LEADER)).thenReturn(belongList);

        // when
        String result = issueService.patchIssueBoard(belong.getMemberId(), issue1.getId(), patchIssueBoardReqDTO);

        // then
        assertEquals("이슈 상태 변경 되었습니다.", result);
    }

    @Test
    @DisplayName("이슈 보드 상태 및 순서 변경 시 이슈 예외 처리")
    public void testPatchIssueInvalidIssue() {
        // given
        Project project = new Project(1L,"project","test project", "image", "kea");
        Belong belong = new Belong(1L, GradeType.LEADER,1L,"harry", Status.ENABLED,project);
        Issue issue1 = new Issue(1L, "issue1","content",IssueStatus.TODO,IssueTag.CHANGED,1,2,project,belong,"null");
        PatchIssueBoardReqDTO patchIssueBoardReqDTO = new PatchIssueBoardReqDTO(0,IssueStatus.TODO,IssueStatus.PROGRESS, IssueCase.COMMON);

        when(issueRepository.findById(issue1.getId())).thenReturn(Optional.of(issue1));
        when(belongRepository.findByProjectIdAndMemberId(project.getId(), belong.getMemberId())).thenReturn(Optional.empty());

        // when, then
        assertThrows(BaseException.class, () -> issueService.patchIssueBoard(belong.getMemberId(), issue1.getId(), patchIssueBoardReqDTO));
    }

    @Test
    @DisplayName("이슈 도넛 차트 조회 성공")
    public void testGetIssueChartSuccess() {
        // given
        Project project = new Project(1L,"project","test project", "image", "kea");
        Belong belong = new Belong(1L, GradeType.LEADER,1L,"harry", Status.ENABLED,project);
        List<Issue> issueList = new ArrayList<>();
        Issue issue1 = new Issue(1L, "issue1","content",IssueStatus.TODO,IssueTag.CHANGED,1,2,project,belong,"null");
        Issue issue2 = new Issue(2L,"issue2","content",IssueStatus.PROGRESS,IssueTag.FIXED,1,1,project,belong,"null");
        issueList.add(issue1);
        issueList.add(issue2);

        when(issueRepository.findByProjectId(project.getId())).thenReturn(issueList);
        when(belongRepository.findByProjectIdAndMemberId(project.getId(), belong.getMemberId())).thenReturn(Optional.of(belong));

        // when
        GetIssueChartResDTO result = issueService.getIssueChart(belong.getMemberId(), project.getId());

        // then
        assertEquals(1, result.getToDoCnt());
        assertEquals(1, result.getProgressCnt());
        assertEquals(0, result.getReviewCnt());
        assertEquals(0, result.getCompleteCnt());
    }

    @Test
    @DisplayName("이슈 도넛 차트 조회 시 소속 예외 처리")
    public void testGetIssueChartInvalidBelong() {
        // given
        Project project = new Project(1L,"project","test project", "image", "kea");
        Belong belong = new Belong(1L, GradeType.LEADER,1L,"harry", Status.ENABLED,project);
        List<Issue> issueList = new ArrayList<>();
        Issue issue1 = new Issue(1L, "issue1","content",IssueStatus.TODO,IssueTag.CHANGED,1,2,project,belong,"null");
        Issue issue2 = new Issue(2L,"issue2","content",IssueStatus.PROGRESS,IssueTag.FIXED,1,1,project,belong,"null");
        issueList.add(issue1);
        issueList.add(issue2);

        when(issueRepository.findByProjectId(project.getId())).thenReturn(issueList);
        when(belongRepository.findByProjectIdAndMemberId(project.getId(), belong.getMemberId())).thenReturn(Optional.empty());

        // when, then
        assertThrows(BaseException.class, () -> issueService.getIssueChart(belong.getMemberId(), issue1.getId()));
    }

    @Test
    @DisplayName("개인 이슈 목록 조회 성공")
    public void testGetMyIssueSuccess() {
        // given
        Project project1 = new Project(1L,"project","test project", "image", "kea");
        Project project2 = new Project(2L,"project2","test project","image","kic");
        Belong belong1 = new Belong(1L, GradeType.LEADER,1L,"harry", Status.ENABLED,project1);
        Belong belong2 = new Belong(2L, GradeType.MEMBER, 1L, "harry",Status.ENABLED,project2);
        List<Issue> issueList1 = new ArrayList<>();
        List<Issue> issueList2 = new ArrayList<>();
        Issue issue1 = new Issue(1L, "issue1","content",IssueStatus.TODO,IssueTag.CHANGED,1,1,project1,belong1,"null");
        Issue issue2 = new Issue(2L,"issue2","content",IssueStatus.PROGRESS,IssueTag.FIXED,1,2,project2,belong2,"null");
        issueList1.add(issue1);
        issueList2.add(issue2);
        List<Belong> belongList = new ArrayList<>();
        belongList.add(belong1);
        belongList.add(belong2);

        when(belongRepository.findByMemberId(1L)).thenReturn(belongList);

        // when
        List<GetMyIssueResDTO> result = issueService.getMyIssue(1L,IssueStatus.TODO, "c");

        // then
        assertEquals(1, result.size());
    }

}

