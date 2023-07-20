package com.batton.projectservice.repository;

import com.batton.projectservice.domain.Belong;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BelongRepository extends JpaRepository<Belong, Long> {
//    static List<Long> findProjectIdsByMemberId(Long memberId);
//    static List<Belong> findAllByProjectIdInAndMemberIdNotIn(Long projectId, Long memberId);
}
