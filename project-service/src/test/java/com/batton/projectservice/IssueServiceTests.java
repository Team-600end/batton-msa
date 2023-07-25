package com.batton.projectservice;

import com.batton.projectservice.domain.Belong;
import com.batton.projectservice.domain.Issue;
import com.batton.projectservice.domain.Project;
import com.batton.projectservice.dto.issue.PostIssueReqDTO;
import com.batton.projectservice.enums.GradeType;
import com.batton.projectservice.enums.IssueStatus;
import com.batton.projectservice.enums.IssueTag;
import com.batton.projectservice.enums.Status;
import com.batton.projectservice.repository.IssueRepository;
import com.batton.projectservice.service.IssueService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class IssueServiceTests {
    @Mock
    IssueRepository issueRepository;
    @InjectMocks
    private IssueService issueService;
    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("이슈 생성")
    public void testAddIssue() {
        // given
        Project project = new Project(1L, "dsfdsf", "sdtre", "rteygf", "ytr");
        Belong belong = new Belong(1L, GradeType.MEMBER, 2L, "sdgert", Status.ENABLED, project);
        Issue issue = new Issue(1L, "sdff", "sfdsfsdf", IssueStatus.TODO, IssueTag.CHANGED, 1, 1, project, belong);
        PostIssueReqDTO postIssueReqDTO = new PostIssueReqDTO(1L, 1L, "sdff", "sff", IssueTag.CHANGED);
        given(issueRepository.save(issue)).willReturn(issue);

        // when
        issueService.postIssue(postIssueReqDTO);

        // then
        verify(issueRepository).save(any());
    }
}





