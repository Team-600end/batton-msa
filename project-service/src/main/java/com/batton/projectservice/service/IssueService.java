package com.batton.projectservice.service;


import com.batton.projectservice.common.BaseException;
import com.batton.projectservice.repository.IssueRepository;
import com.batton.projectservice.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class IssueService {
    private final IssueRepository issueRepository;
}
