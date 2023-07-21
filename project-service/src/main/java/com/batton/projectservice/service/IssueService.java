package com.batton.projectservice.service;

import com.batton.projectservice.common.BaseException;
import com.batton.projectservice.domain.Belong;
import com.batton.projectservice.domain.Issue;
import com.batton.projectservice.domain.Project;
import com.batton.projectservice.dto.issue.PostIssueReqDTO;
import com.batton.projectservice.enums.IssueStatus;
import com.batton.projectservice.repository.BelongRepository;
import com.batton.projectservice.repository.IssueRepository;
import com.batton.projectservice.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

import static com.batton.projectservice.common.BaseResponseStatus.PROJECT_BELONG_NOT_FOUND;

@RequiredArgsConstructor
@Service
public class IssueService {
    private final IssueRepository issueRepository;
    private final ProjectRepository projectRepository;
    private final BelongRepository belongRepository;

    /**
     * 이슈 생성
     */
    @Transactional
    public Long addIssue(PostIssueReqDTO postIssueReqDTO) {
        Optional<Project> project = projectRepository.findById(postIssueReqDTO.getProjectId());
        Optional<Belong> belong = belongRepository.findById(postIssueReqDTO.getBelongId());
        List<Issue> issues = issueRepository.findByIssueStatus(IssueStatus.TODO);

        int issueSeq = issues.size()+1;

        if (project.isPresent() && belong.isPresent()) {
            Issue issue = postIssueReqDTO.toEntity(project.get(), belong.get(), postIssueReqDTO, IssueStatus.TODO, issueSeq);
            Long issueId = issueRepository.save(issue).getId();

            return issueId;
        } else {
            throw new BaseException(PROJECT_BELONG_NOT_FOUND);
        }
    }
}
