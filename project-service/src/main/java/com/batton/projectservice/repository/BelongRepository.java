package com.batton.projectservice.repository;

import com.batton.projectservice.domain.Belong;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BelongRepository extends JpaRepository<Belong, Long> {
    Optional<Belong> findByProjectIdAndMemberId(Long projectId, Long memberId);

    @Query("SELECT b FROM Belong b WHERE b.project.id = :projectId AND b.memberId != :myMemberId")
    List<Belong> findBelongsByProjectId(@Param("projectId") Long projectId, @Param("myMemberId") Long myMemberId);

    Optional<Belong> findById(Long belongId);

    List<Optional<Belong>> findByMemberId(Long memberId);
}
