package com.batton.projectservice.repository;

import com.batton.projectservice.domain.Issue;
import com.batton.projectservice.enums.IssueStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IssueRepository extends JpaRepository<Issue, Long> {
    List<Issue> findByIssueStatusOrderByIssueSeq(IssueStatus status);
    List<Issue> findByProjectId(Long projectId);
    //사용자가 담당한 이슈 리스트를 마지막 수정 날짜 내림차순으로 반환
    List<Issue> findByBelongIdOrderByUpdatedAtDesc(Long belongId);
    //해당 프로젝트의 이슈가 있는지 확인
    boolean existsByProjectId(Long projectId);
    //해당 프로젝트의 이슈 중 가장 최신의 이슈 반환
    Issue findTopByProjectIdOrderByCreatedAtDesc(Long projectId);
    //해당 프로젝트의 이슈 리스트를 마지막 수정 날짜 내림차순으로 변환
    List<Issue> findByProjectIdOrderByUpdatedAtDesc(Long projectId);
    Optional<Issue> findById(Long id);
}
