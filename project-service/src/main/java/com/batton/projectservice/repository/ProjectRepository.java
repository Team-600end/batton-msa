package com.batton.projectservice.repository;

import com.batton.projectservice.domain.Belong;
import com.batton.projectservice.domain.Project;
import com.batton.projectservice.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    boolean existsByProjectKey(String projectKey);

//    @Query("SELECT DISTINCT b.project.id FROM Belong b WHERE b.memberId = ?1")
//    List<Long> findProjectIdsByMemberId(Long memberId);
//
//    @Query("SELECT DISTINCT b.memberId FROM Belong b WHERE b.project.id IN (?1) AND b.memberId <> ?2")
//    List<Long> findOtherMemberByProjectId(List<Long> projectIds, Long memberId);

}
