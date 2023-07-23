package com.batton.projectservice.repository;

import com.batton.projectservice.domain.Issue;
import com.batton.projectservice.enums.IssueStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IssueRepository extends JpaRepository<Issue, Long> {
    List<Issue> findByIssueStatusOrderByIssueSeq(IssueStatus status);
}
